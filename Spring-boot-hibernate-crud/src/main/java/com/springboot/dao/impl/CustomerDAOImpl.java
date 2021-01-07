package com.springboot.dao.impl;

import com.springboot.dao.CustomerDAO;
import com.springboot.entity.Customer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class CustomerDAOImpl implements CustomerDAO {

    private EntityManager entityManager;

    @Autowired
    public CustomerDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Customer> getCustomers() {

        Session currentSession = entityManager.unwrap(Session.class);
        Query<Customer> theQuery = currentSession.createQuery("from Customer order by lastName", Customer.class);
        List<Customer> customers = theQuery.getResultList();
        return customers;
    }

    @Override
    public void saveCustomer(Customer customer) {
        Session currentSession = entityManager.unwrap(Session.class);

        currentSession.saveOrUpdate(customer);
    }

    @Override
    public Customer getCustomer(int theId) {
        Session currentSession = entityManager.unwrap(Session.class);

        Customer customer = currentSession.get(Customer.class, theId);

        return customer;
    }

    @Override
    public void deleteCustomer(int theId) {
        Session currentSession = entityManager.unwrap(Session.class);

        Customer customer = currentSession.get(Customer.class, theId);
        currentSession.delete(customer);
//        Query theQuery = currentSession.createQuery("delete from Customer where id=:customerId");
//        theQuery.setParameter("customerId", theId);
//        theQuery.executeUpdate();

    }

    @Override
    public List<Customer> searchCustomers(String theSearchName) {
        // get the current hibernate session
        Session currentSession = entityManager.unwrap(Session.class);

        Query theQuery = null;

        //
        // only search by name if theSearchName is not empty
        //
        if (theSearchName != null && theSearchName.trim().length() > 0) {

            // search for firstName or lastName ... case insensitive
            theQuery = currentSession.createQuery("from Customer where lower(firstName) like :theName or lower(lastName) like :theName", Customer.class);
            theQuery.setParameter("theName", "%" + theSearchName.toLowerCase() + "%");

        } else {
            // theSearchName is empty ... so just get all customers
            theQuery = currentSession.createQuery("from Customer", Customer.class);
        }

        // execute query and get result list
        List<Customer> customers = theQuery.getResultList();

        // return the results
        return customers;
    }

}
