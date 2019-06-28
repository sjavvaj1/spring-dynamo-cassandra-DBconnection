package com.springboot.repository;


import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.springboot.domain.OrderCassandra;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Repository
public class OrderCassandraRepositoryImpl implements OrderCassandraRepository {

    @Autowired
    CassandraOperations cassandraOperations;

    @Value("${cassandra.table}")
    private String cassandraTable;

    @Value("${cassandra.keyspace}")
    private String cassandraKeySpace;

    @Override
    public OrderCassandra createOrderCassandra(OrderCassandra orderCassandra) {

        System.out.println("inserting order into cassandra -->  " + orderCassandra.getOrderId());

        OrderCassandra updatedOrderCassandra = cassandraOperations.insert(orderCassandra);

        return updatedOrderCassandra;
    }

    @Override
    public OrderCassandra getOrderCassandra(String orderId) {

        System.out.println("retrieving order from cassandra : " + orderId);

        Select selectQuery = QueryBuilder.select().all().from(cassandraKeySpace, cassandraTable);

        selectQuery.where(QueryBuilder.eq("orderId", orderId));

        List<OrderCassandra> orderCassandraList = cassandraOperations.select(selectQuery, OrderCassandra.class);

        System.out.println("orderCassandraList -->> " + orderCassandraList.get(0).getFirstName());

        // We can handle it better
        if (!CollectionUtils.isEmpty(orderCassandraList)) {
            return orderCassandraList.get(0);
        } else {
            return null;
        }
    }
}
