package com.zetres.elections.aop;

import com.zetres.elections.domain.GeoCentre;
import com.zetres.elections.model.GeoCentreDTO;
import com.zetres.elections.model.GeoLocaliteDTO;
import com.zetres.elections.repos.BackupRecordsDAO;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;

@Aspect
public class BackupCRUDOpAspect {

    @Autowired private BackupRecordsDAO backupRecordsDAO;


    @Around("execution(* com.zetres.elections.service.*.create(..))")
    public Object backupInsertions(ProceedingJoinPoint point) throws Throwable{
        Object result = null;
        var insertion = "";
        var args = point.getArgs();
        var objectToRecord = args[0];
       if(objectToRecord instanceof GeoLocaliteDTO geoLocaliteDTO){
            insertion = String.format("INSERT INTO GEO_LOCALITE (PK, CODE, NAME, fk_canton, DATE_ADDED, ACTIVE) " +
                            "VALUES(%s, '%s', '%s', '%s', '%s', '%s')",
                    geoLocaliteDTO.getPk(),
                    geoLocaliteDTO.getCode(),
                    geoLocaliteDTO.getName(),
                    geoLocaliteDTO.getFkCanton(),
                    geoLocaliteDTO.getDateAdded(),
                    geoLocaliteDTO.getActive()
            );
        }

        result = point.proceed();
        if(result instanceof GeoCentre geoCentre){
            insertion = String.format("INSERT INTO GEO_CENTRE (NAME, KITIDBEGIN, FULLKITIDBEGIN, fk_localite, PK, DATE_ADDED, ACTIVE) " +
                                                            "VALUES(%s, '%s', '%s', '%s', '%s', '%s', '%s')",
                            geoCentre.getName(),
                            geoCentre.getKitidbegin(),
                            geoCentre.getFullkitidbegin(),
                            geoCentre.getFkLocalite().getPk(),
                            geoCentre.getPk(),
                            geoCentre.getDateAdded(),
                            geoCentre.getActive()
            );
        }
        backupRecordsDAO.insert(insertion);
        return result;
    }

    @Around("execution(* com.zetres.elections.service.*.update(..))")
    public Object backupUpdates(ProceedingJoinPoint point) throws Throwable{
        Object result = null;
        var updates = "";
        var args = point.getArgs();
        var objectToUpdatePK = args[0];
        var objectToUpdate = args[1];
        if(objectToUpdate instanceof GeoCentreDTO geoCentreDTO){
            updates = String.format(
                            "UPDATE GEO_CENTRE SET (ACTIVE, DATE_ADDED, FK_LOCALITE, FUllKITIDBEGIN, KITIDBEGIN, NAME)" +
                            " = (%s, '%s', '%s', '%s', '%s', '%s')" +
                            " WHERE PK='%s'",
                            geoCentreDTO.getActive(),
                            geoCentreDTO.getDateAdded(),
                            geoCentreDTO.getFkLocalite(),
                            geoCentreDTO.getFullkitidbegin(),
                            geoCentreDTO.getFullkitidbegin(),
                            geoCentreDTO.getName(),
                            geoCentreDTO.getPk()
            );
        }else if(objectToUpdate instanceof  GeoLocaliteDTO geoLocaliteDTO){
            updates = String.format(
                            "UPDATE GEO_LOCALITE SET (CODE, NAME, fk_canton, DATE_ADDED, ACTIVE)" +
                            " = ('%s', '%s', '%s', '%s', '%s')" +
                            " WHERE PK='%s'",
                    geoLocaliteDTO.getCode(),
                    geoLocaliteDTO.getName(),
                    geoLocaliteDTO.getFkCanton(),
                    geoLocaliteDTO.getDateAdded(),
                    geoLocaliteDTO.getActive()
            );
        }
        backupRecordsDAO.insert(updates);
        result = point.proceed();
        return result;
    }
    @Around("execution(* com.zetres.elections.service.*.delete(..))")
    public Object backupDelete(ProceedingJoinPoint point) throws Throwable{
        Object result = null;
        var updates = "";
        var args = point.getArgs();
        var objectToUpdatePK = args[0];
        switch (point.getSignature().getDeclaringTypeName()) {
            case "com.zetres.elections.service.GeoCentreService" -> updates =
                    String.format(
                        "DELETE FROM GEO_CENTRE WHERE PK='%s'",
                        objectToUpdatePK);
            case "com.zetres.elections.service.GeoLocaliteService" -> updates =
                    String.format(
                        "DELETE FROM GEO_LOCALITE WHERE PK='%s'",
                        objectToUpdatePK);
        }
        backupRecordsDAO.insert(updates);
        result = point.proceed();
        return result;
    }
}