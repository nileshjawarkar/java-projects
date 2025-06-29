package co.in.nnj.learn.jee.adapter.output.object_mapper;

import java.util.List;

import co.in.nnj.learn.jee.adapter.output.db.entity.AddressEntity;
import co.in.nnj.learn.jee.domain.valueobjects.Address;

public class AddressMapper implements EntityMapper<AddressEntity, Address> {
    @Override
    public AddressEntity updateEntity(final AddressEntity entity, final Address vobj) {
        throw new UnsupportedOperationException("Unimplemented method 'updateEntity'");
    }

    @Override
    public AddressEntity toEntity(final Address obj) {
        if(obj == null) {
            return null;
        }
        final AddressEntity entity = new AddressEntity();
        entity.setStreet(obj.street());
        entity.setCity(obj.city());
        entity.setPin(obj.pin());
        entity.setState(obj.state());
        entity.setCountry(obj.country());
        entity.setLandscape(obj.landscape());
        return entity;
    }

    @Override
    public Address toValue(final AddressEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Address(entity.getId(), entity.getStreet(), entity.getCity(), entity.getState(), entity.getCountry(),
                entity.getPin(), entity.getLandscape());
    }

    @Override
    public List<Address> toValueList(final List<AddressEntity> listOfEntities) {
        throw new UnsupportedOperationException("Unimplemented method 'toValueList'");
    }
}
