package com.nnj.learn.jee.control;

import java.util.ArrayList;
import java.util.List;

import jakarta.ejb.Stateless;

import com.nnj.learn.jee.entity.Car;
import com.nnj.learn.jee.entity.Category;
import com.nnj.learn.jee.entity.Seat;
import com.nnj.learn.jee.entity.SeatBelt;
import com.nnj.learn.jee.entity.SeatBeltModel;
import com.nnj.learn.jee.entity.SeatMaterial;
import com.nnj.learn.jee.entity.Specification;

@Stateless
public class CarFactory {
    public Car createCar(final Specification spec) {
        final Car car = new Car();
        // -- car.setId(UUID.randomUUID().toString());
        car.setEngineType(spec.getEngineType());
        car.setColor(spec.getColor());

        final SeatBelt b1 = new SeatBelt();
        final SeatBelt b2 = new SeatBelt();
        final SeatBelt b3 = new SeatBelt();
        final SeatBelt b4 = new SeatBelt();
        final SeatBelt b5 = new SeatBelt();

        final Seat fl = new Seat();
        final Seat fr = new Seat();
        final Seat bl = new Seat();
        final Seat bm = new Seat();
        final Seat br = new Seat();

        final Category category = spec.getCategory();
        if (category == null || category == Category.BASE) {
            fl.setMaterial(SeatMaterial.FABRIC);
            fr.setMaterial(SeatMaterial.FABRIC);
            bl.setMaterial(SeatMaterial.FABRIC);
            bm.setMaterial(SeatMaterial.FABRIC);
            br.setMaterial(SeatMaterial.FABRIC);

            b1.setModel(SeatBeltModel.NORMAL);
            b2.setModel(SeatBeltModel.NORMAL);
            b3.setModel(SeatBeltModel.NORMAL);
            b4.setModel(SeatBeltModel.NORMAL);
            b5.setModel(SeatBeltModel.NORMAL);

        } else if (category == Category.MIDDLE) {
            fl.setMaterial(SeatMaterial.VINYL);
            fr.setMaterial(SeatMaterial.VINYL);
            bl.setMaterial(SeatMaterial.VINYL);
            bm.setMaterial(SeatMaterial.VINYL);
            br.setMaterial(SeatMaterial.VINYL);

            b1.setModel(SeatBeltModel.NORMAL_WITH_AIRBAGS);
            b2.setModel(SeatBeltModel.NORMAL_WITH_AIRBAGS);
            b3.setModel(SeatBeltModel.NORMAL_WITH_AIRBAGS);
            b4.setModel(SeatBeltModel.NORMAL_WITH_AIRBAGS);
            b5.setModel(SeatBeltModel.NORMAL_WITH_AIRBAGS);

        } else if (category == Category.PREMIUM || category == Category.LUXURY) {
            fl.setMaterial(SeatMaterial.LEATHER);
            fr.setMaterial(SeatMaterial.LEATHER);
            bl.setMaterial(SeatMaterial.LEATHER);
            bm.setMaterial(SeatMaterial.LEATHER);
            br.setMaterial(SeatMaterial.LEATHER);

            b1.setModel(SeatBeltModel.ADV_WITH_AIRBAGS);
            b2.setModel(SeatBeltModel.ADV_WITH_AIRBAGS);
            b3.setModel(SeatBeltModel.ADV_WITH_AIRBAGS);
            b4.setModel(SeatBeltModel.ADV_WITH_AIRBAGS);
            b5.setModel(SeatBeltModel.ADV_WITH_AIRBAGS);
        }

        fl.setBelt(b1);
        fr.setBelt(b2);
        bl.setBelt(b3);
        bm.setBelt(b4);
        br.setBelt(b5);

        final List<Seat> seats = new ArrayList<>(5);
        seats.add(fl);
        seats.add(fr);
        seats.add(bl);
        seats.add(bm);
        seats.add(br);
        car.setSeats(seats);
        return car;
    }
}
