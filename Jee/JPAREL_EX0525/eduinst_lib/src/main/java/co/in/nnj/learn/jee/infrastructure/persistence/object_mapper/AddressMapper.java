package co.in.nnj.learn.jee.infrastructure.persistence.object_mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.in.nnj.learn.jee.domain.Address;
import co.in.nnj.learn.jee.infrastructure.persistence.entity.AddressEntity;

public class AddressMapper extends EntityMapper<AddressEntity, Address> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AddressMapper.class.getName());

    @Override
    public AddressEntity updateEntity(final AddressEntity entity, final Address vobj) {
        throw new UnsupportedOperationException("Unimplemented method 'updateEntity'");
    }

    @Override
    public AddressEntity toEntity(final Address obj) {
        if (obj == null) {
            return null;
        }
        final AddressEntity entity = new AddressEntity();
        entity.setId(obj.id());
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
        LOGGER.info(entity.toString());
        return new Address(entity.getId(), entity.getStreet(), entity.getCity(), entity.getState(), entity.getCountry(),
                entity.getPin(), entity.getLandscape());
    }
}
