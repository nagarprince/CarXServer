package com.LoginPage.login.CarFetch;

import com.LoginPage.login.CarFetch.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepo extends JpaRepository<Car, Long> {

}
