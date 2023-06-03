package com.driver.services.impl;

import com.driver.model.Admin;
import com.driver.model.Country;
import com.driver.model.CountryName;
import com.driver.model.ServiceProvider;
import com.driver.repository.AdminRepository;
import com.driver.repository.CountryRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService{
    @Autowired
    AdminRepository adminRepository1;

    @Autowired
    ServiceProviderRepository serviceProviderRepository1;

    @Autowired
    CountryRepository countryRepository1;

    @Override
    public Admin register(String username, String password) {
        Admin admin = new Admin();
        admin.setUsername(username);
        admin.setPassword(password);

        return adminRepository1.save(admin);
    }

    @Override
    public Admin addServiceProvider(int adminId, String providerName) {
       /* Optional<Admin> optionalAdmin = adminRepository1.findById(adminId);
        Admin admin = optionalAdmin.get();
        ServiceProvider serviceProvider = serviceProviderRepository1.findByName(providerName);

        admin.getServiceProviders().add(serviceProvider);
        serviceProvider.setAdmin(admin);

        return admin;*/

        Admin admin = adminRepository1.findById(adminId).get();
        ServiceProvider serviceProvider = new ServiceProvider();

        serviceProvider.setName(providerName);
        serviceProvider.setAdmin(admin);
        List<ServiceProvider> serviceProviderList = admin.getServiceProviders();
        serviceProviderList.add(serviceProvider);
        admin.setServiceProviders(serviceProviderList);

        return adminRepository1.save(admin);
    }

    @Override
    public ServiceProvider addCountry(int serviceProviderId, String countryName) throws Exception{

        String countryName1 = countryName.toUpperCase();
        if (!countryName1.equals("IND") && !countryName1.equals("USA") && !countryName1.equals("CHI") && !countryName1.equals("JPN")) throw new Exception("Country not found");

        ServiceProvider serviceProvider = serviceProviderRepository1.findById(serviceProviderId).get();
        Country country = new Country(CountryName.valueOf(countryName1),CountryName.valueOf(countryName1).toCode());
        country.setServiceProvider(serviceProvider);
        serviceProvider.getCountryList().add(country);
        return serviceProviderRepository1.save(serviceProvider);
    }
}
