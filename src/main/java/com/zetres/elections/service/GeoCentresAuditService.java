package com.zetres.elections.service;

import com.zetres.elections.domain.GeoCentre;
import com.zetres.elections.repos.GeoCentresAuditRepository;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.StringJoiner;

@Service
@Transactional(readOnly = true)
public class GeoCentresAuditService {
    private GeoCentresAuditRepository geoCentresAuditRepository;
    @Autowired
    private AuditReader auditReader;

    public GeoCentresAuditService(GeoCentresAuditRepository geoCentresAuditRepository) {
        this.geoCentresAuditRepository = geoCentresAuditRepository;
    }


    public String findRevisions(String id){
        var sj = new StringJoiner(",", "[", "]");
        var entities = auditReader.createQuery()
                .forRevisionsOfEntityWithChanges(GeoCentre.class, true)
                .getResultList();
        int cnt=0;
        var newLocationId = "";
        var oldLocationId = "";
        for(var entity : entities){
            cnt++;
            var array = (Object[])entity;
            var geoCentre = (GeoCentre)array[0];
            var revisionsEnt = (DefaultRevisionEntity) array[1];
            var revType = (RevisionType) array[2];
            var propChanged = (HashSet) array[3];
            String firstLocationId = null;
            var sj2 = new StringJoiner(",", "{", "}");
            if(revType==RevisionType.ADD){
                firstLocationId = geoCentre.getFkLocalite().getPk();
            }
            if(revType==RevisionType.MOD){
                if(propChanged.contains("fkLocalite")){
                    //MOVE
                    newLocationId = geoCentre.getFkLocalite().getPk();
                    sj2.add(" \"revisionId\" : \""+String.valueOf(revisionsEnt.getId())+"\" ");
                    sj2.add("\"guid\":\""+geoCentre.getPk()+"\"");
                    sj2.add("\"createdAt\":\""+revisionsEnt.getRevisionDate().toString()+"\"");
                    sj2.add("\"oldLocationId\":\""+ oldLocationId +"\"");
                    sj2.add("\"newLocationId\":\""+newLocationId+"\"");
                    oldLocationId=newLocationId;
                    //TODO: create enum
                    sj2.add("\"type\":"+"\"MOVE\"");
                    //TODO: Principal
                    sj2.add("\"operator\":"+"\"operatorTest\"");
                    sj2.add("\"operatorID\":\"1111-1111-1111-test\"");
                    sj2.add("\"reason\":\"test\"");
                    sj.add(sj2.toString());
                }
            }
        }
        return sj.toString();
    }


    public String getAllRevisionsAfterVersionId(String versionId){
        var sj = new StringJoiner(",", "[", "]");
        var entities = auditReader.createQuery()
                .forRevisionsOfEntityWithChanges(GeoCentre.class, true)
                .getResultList();
        var currentVersion = auditReader.getRevisionNumberForDate(Timestamp.valueOf(LocalDateTime.now()));
        int cnt=0;
        var newLocationId = "";
        var oldLocationId = "";
        for(var entity : entities){
            cnt++;
            var array = (Object[])entity;
            var geoCentre = (GeoCentre)array[0];
            var revisionsEnt = (DefaultRevisionEntity) array[1];
            var revType = (RevisionType) array[2];
            var propChanged = (HashSet) array[3];
            if(revisionsEnt.getId()>=Integer.parseInt(versionId) && revisionsEnt.getId() <= currentVersion.intValue()){
                String firstLocationId = null;
                var sj2 = new StringJoiner(",", "{", "}");
                sj2.add(" \"revisionId\" : \""+String.valueOf(revisionsEnt.getId())+"\" ");
                sj2.add("\"guid\":\""+geoCentre.getPk()+"\"");
                sj2.add("\"createdAt\":\""+revisionsEnt.getRevisionDate().toString()+"\"");
                if(revType==RevisionType.ADD){
                    firstLocationId = geoCentre.getFkLocalite().getPk();
                    sj2.add("\"type\":"+"\"ADD\"");
                }
                if(revType==RevisionType.MOD){
                    if(propChanged.contains("fkLocalite")){
                        //MOVE
                        newLocationId = geoCentre.getFkLocalite().getPk();
                        sj2.add("\"oldLocationId\":\""+ oldLocationId +"\"");
                        sj2.add("\"newLocationId\":\""+newLocationId+"\"");
                        oldLocationId=newLocationId;
                        //TODO: create enum
                        sj2.add("\"type\":"+"\"MOVE\"");
                    }
                }
                if(revType==RevisionType.DEL){
                    sj2.add("\"type\":"+"\"DELETE\"");
                }
                //TODO: Principal
                sj2.add("\"operator\":"+"\"operatorTest\"");
                sj2.add("\"operatorID\":\"1111-1111-1111-test\"");
                sj2.add("\"reason\":\"test\"");
                sj.add(sj2.toString());
            }
        }
        return sj.toString();
    }

}
