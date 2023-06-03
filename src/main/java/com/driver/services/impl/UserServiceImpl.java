package com.driver.services.impl;

import com.driver.model.Country;
import com.driver.model.CountryName;
import com.driver.model.ServiceProvider;
import com.driver.model.User;
import com.driver.repository.CountryRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.repository.UserRepository;
import com.driver.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository3;
    @Autowired
    ServiceProviderRepository serviceProviderRepository3;
    @Autowired
    CountryRepository countryRepository3;

    @Override
    public User register(String username, String password, String countryName) throws Exception{
        //convert to uppercase to ignore uppercase or lower case
        String countryName1 = countryName.toUpperCase();
        //Throw exception
        if (!countryName1.equals("IND") && !countryName1.equals("USA") && !countryName1.equals("CHI") && !countryName1.equals("JPN"))
            throw new Exception("Country not found");

        Country country = new Country(CountryName.valueOf(countryName1),CountryName.valueOf(countryName1).toCode());


        //Create user and set values
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setConnected(false);


        //add user to country
        country.setUser(user);
        user.setOriginalCountry(country);
        //save user to get user obejct and by that userId
        user = userRepository3.save(user);
        //save the originalIp in given format
        user.setOriginalIp(user.getOriginalCountry().getCode() + "." + user.getId());
        //save user
        return userRepository3.save(user);
    }

    @Override
    public User subscribe(Integer userId, Integer serviceProviderId) {
        ServiceProvider serviceProvider = serviceProviderRepository3.findById(serviceProviderId).get();
        Optional<User> optionalUser = userRepository3.findById(userId);
        User user = optionalUser.get();

        user.getServiceProviderList().add(serviceProvider);
        User savedUser = userRepository3.save(user);

        serviceProvider.getUsers().add(savedUser);

        return user;
    }
}
