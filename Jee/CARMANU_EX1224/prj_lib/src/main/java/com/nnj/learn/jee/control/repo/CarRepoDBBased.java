package com.nnj.learn.jee.control.repo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.nnj.learn.jee.entity.Car;
import com.nnj.learn.jee.entity.Color;
import com.nnj.learn.jee.entity.EngineType;

public class CarRepoDBBased implements CarRepo {

    final DataSource dataSource;

    public CarRepoDBBased(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void store(final Car car) {
        try (final Connection connection = dataSource.getConnection()) {
            final PreparedStatement stmt = connection.prepareStatement("""
                    INSERT INTO cars \
                    (id, color, engine) \
                    VALUES (?, ?, ?)""");
            stmt.setString(1, car.getIdentifier());
            stmt.setString(2, car.getColor().toString());
            stmt.setString(3, car.getEngineType().toString());
            stmt.executeUpdate();
        } catch (final SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Car getCar(final String id) {
        try (final Connection connection = dataSource.getConnection()) {
            final PreparedStatement stmt = connection.prepareStatement("SELECT * FROM cars WHERE id = ? ORDER BY id");
            stmt.setString(1, id);
            final ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                final Car car = new Car(id);
                car.setColor(Color.valueOf(rs.getString("color")));
                car.setEngineType(EngineType.valueOf(rs.getString("engine")));
                return car;
            }
        } catch (final SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Car> getCars(final Integer limit, final Integer offset) {
        try (final Connection connection = dataSource.getConnection()) {
            final StringBuilder sb = new StringBuilder("SELECT * FROM cars ORDER BY id");
            if (limit != null && limit > 0) {
                sb.append(" LIMIT ");
                sb.append(limit);
            }
            if (offset != null && offset > 0) {
                sb.append(" OFFSET ");
                sb.append(offset);
            }
            final PreparedStatement stmt = connection.prepareStatement(sb.toString());
            final ResultSet rs = stmt.executeQuery();
            final List<Car> cars = new ArrayList<>();
            while (rs.next()) {
                final Car car = new Car(rs.getString("id"));
                car.setColor(Color.valueOf(rs.getString("color")));
                car.setEngineType(EngineType.valueOf(rs.getString("engine")));
                cars.add(car);
            }
            return cars;
        } catch (final SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void cleanup() {
    }

    @Override
    public void setup() {
        try (Connection con = dataSource.getConnection()) {
            final PreparedStatement stmt = con.prepareStatement("""
                    CREATE TABLE cars(id varchar(36) primary key, engine varchar(255), color varchar(255) )""");
            stmt.executeUpdate();
        } catch (final SQLException e) {
            e.printStackTrace();
        }
    }

}
