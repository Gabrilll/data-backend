package com.judicature.databackend.data.semantic;

import org.apache.jena.ontology.Individual;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Gabri
 */

@Repository
public class ConstructionDAOImpl extends ConstructionBaseDAOImpl implements ConstructionDAO {

    @Override
    public boolean addDiseaseObjectProperty(Individual individualStart, String relationName, Individual individualEnd) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean addDiseaseObjectProperties(Individual individualStart, List<String> relationName, List<Individual> individualEnd) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean addOrganizationProperty(Individual individualStart, String relationName, Individual individualEnd) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean addOrganizationProperties(Individual individualStart, List<String> relationName, List<Individual> individualEnd) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean addPersonProperty(Individual individualStart,
                                      String relationName, Individual individualEnd) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean addPersonProperties(Individual individualStart,
                                        List<String> relationName, List<Individual> individualEnd) {
        // TODO Auto-generated method stub
        return false;
    }


}