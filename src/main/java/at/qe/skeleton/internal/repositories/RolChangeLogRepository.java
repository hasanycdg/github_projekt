package at.qe.skeleton.internal.repositories;

import at.qe.skeleton.internal.model.RolChangeLog;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public interface RolChangeLogRepository extends AbstractRepository <RolChangeLog, Long>, Serializable {
    public RolChangeLog findAllById (Long id);
    @Query("SELECT DISTINCT rcl.id FROM RolChangeLog rcl")
    public List<Long> findAllId();
}