package com.example.Inventory.security;

import com.example.Inventory.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserPrincipalDetailsService implements UserDetailsService {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Query query = new Query();
        query.addCriteria(Criteria.where("user_id").is(userId));
        User user = mongoTemplate.findOne(query, User.class);
        UserPrincipal userPrincipal = new UserPrincipal(user);
        return userPrincipal;
    }
}
