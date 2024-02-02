package at.qe.skeleton.internal.repositories;

import at.qe.skeleton.internal.model.FavLocation;
import at.qe.skeleton.internal.model.Userx;

import java.io.Serializable;
import java.util.List;

public interface FavLocationRepository  extends AbstractRepository<FavLocation,String>, Serializable  {
    public List<FavLocation> findAllByUser(Userx user);
    public FavLocation findFirstById(Long id);
    public FavLocation findFirstByNameAndUser(String name, Userx userx);
}
