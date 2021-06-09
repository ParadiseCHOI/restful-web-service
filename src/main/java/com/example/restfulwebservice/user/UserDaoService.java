package com.example.restfulwebservice.user;


import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service
public class UserDaoService {
    private static List<User> users = new ArrayList<>();
    private static int userCount = 3;

    static {
        users.add(new User(1, "Paradise", new Date(), "pass1", "701010-1111111"));
        users.add(new User(2, "Nak", new Date(), "pass2", "801010-2222222"));
        users.add(new User(3, "Choi", new Date(), "pass3", "901010-1111111"));
    }

    public List<User> findAll(){
        return users;
    }

    public User save(User user) {
        if (user.getId() == null) {
            user.setId(++userCount);
            user.setJoinDate(new Date());
        }

        users.add(user);
        return user;
    }

    public User findOne(int id) {
        for (User user: users) {
            if (user.getId() == id) {
                return user;
            }
        }

        return null;
    }

    public User deleteById(int id) {
        Iterator<User> iterator = users.iterator();
        while (iterator.hasNext()) {
            User user = iterator.next();

            if (user.getId() == id) {
                iterator.remove();
                userCount--;
                return user;
            }
        }

        return null;
    }

    public User update(User user, int id) {
        User updatedUser = this.findOne(id);

        if (updatedUser != null && user.getId() == id) {
            updatedUser.setName(user.getName());
            return updatedUser;
        }

        return null;
    }
}
