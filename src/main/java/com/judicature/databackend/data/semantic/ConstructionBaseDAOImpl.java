package com.judicature.databackend.data.semantic;

import com.judicature.databackend.config.SemanticConfig;
import com.judicature.databackend.util.Alias;
import com.judicature.databackend.util.Dict;
import org.apache.jena.ontology.*;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Resource;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Gabri
 */
@Repository
public class ConstructionBaseDAOImpl implements ConstructionBaseDAO {

    public static final OntModel MODEL = SemanticConfig.getInstance().model;

    public static List<String> dict= Dict.getDict();
    public static Map<String,String> alias= Alias.getAlias();
    SemanticConfig semanticConfig=SemanticConfig.getInstance();

    @Override
    public OntClass createOntClass(String className) {
        return MODEL.createClass(semanticConfig.getPizzaNs() + className);
    }

    @Override
    public OntClass getOntClass(String ontClassName) {
        return MODEL.getOntClass(semanticConfig.getPizzaNs() + ontClassName);
    }


    @Override
    public void addSameAs(Individual individual, Individual aliasIndividual) {
        individual.addSameAs(aliasIndividual);
    }

    @Override
    public void addSubClass(OntClass ontClass, OntClass subClass) {

        ontClass.addSubClass(subClass);
    }

    @Override
    public void addSubClass(Individual individual, OntClass ontClass) {
        individual.addOntClass(ontClass);
    }

    @Override
    public Individual createIndividual(String individualId, OntClass genusClass) {
        return MODEL.createIndividual(semanticConfig.getPizzaNs() + individualId, genusClass);
    }

    @Override
    public Individual getIndividual(String individualId) {
        return MODEL.getIndividual(semanticConfig.getPizzaNs() + individualId);
    }

    @Override
    public boolean addComment(Individual individual, String comment) {
        Literal ccommentLiteral = MODEL.createLiteral(comment);
        individual.addComment(ccommentLiteral);
        File file = new File(semanticConfig.getConstruct_ontologyPath());
        FileLock fileLock=SemanticConfig.getfilelock(file);

        FileWriter fw = null;
        try {
            fw = new FileWriter(file);
            MODEL.write(fw, "RDF/XML-ABBREV");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if(fileLock!=null){
            try {
                fileLock.release();
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        return true;
    }

    @Override
    public boolean addDataProperty(Individual individual, String propertyName, String propertyValue) {
        if (propertyName == null || propertyValue == null) {
            return false;
        }
        DatatypeProperty property = MODEL.createDatatypeProperty(semanticConfig.getPizzaNs() + "有" + propertyName);
        individual.addProperty(property, propertyValue);
        File file = new File(semanticConfig.getConstruct_ontologyPath());
        FileLock fileLock=SemanticConfig.getfilelock(file);

        FileWriter fw = null;
        try {
            fw = new FileWriter(file);
            MODEL.write(fw, "RDF/XML-ABBREV");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if(fileLock!=null){
            try {
                fileLock.release();
            }catch (Exception e){
                e.printStackTrace();
            }

        }

        return true;
    }

    @Override
    public boolean addDataProperties(Individual individual, List<String> propertyNames, List<String> propertyValues) {
        if (propertyNames == null || propertyValues == null) {
            return false;
        }
        for (int i = 0; i < propertyNames.size(); i++) {
            DatatypeProperty property = MODEL.createDatatypeProperty(semanticConfig.getPizzaNs() + "有" + propertyNames.get(i));
            individual.addProperty(property, propertyValues.get(i));
        }

        File file = new File(semanticConfig.getConstruct_ontologyPath());
        FileLock fileLock=SemanticConfig.getfilelock(file);


        FileWriter fw = null;
        try {
            fw = new FileWriter(file);
            MODEL.write(fw, "RDF/XML-ABBREV");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if(fileLock!=null){
            try {
                fileLock.release();
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        return true;
    }

    @Override
    public Resource addObjectProperty(Individual individualStart, String objectPropertyName, Individual individualEnd) {
        if (individualStart == null || objectPropertyName == null || individualEnd == null) {
            return null;
        }
        ObjectProperty property = MODEL.createObjectProperty(semanticConfig.getPizzaNs() + objectPropertyName);
        Resource resource = individualStart.addProperty(property, individualEnd);

        File file = new File(semanticConfig.getConstruct_ontologyPath());
        FileLock fileLock=SemanticConfig.getfilelock(file);

        FileWriter fw = null;
        try {
            fw = new FileWriter(file);
            MODEL.write(fw, "RDF/XML-ABBREV");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if(fileLock!=null){
            try {
                fileLock.release();
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        return resource;
    }

    @Override
    public List<Resource> addObjectProperties(Individual individualStart, List<String> objectPropertyNames, List<Individual> individualEnds) {
        if (individualStart == null || objectPropertyNames == null || individualEnds == null) {
            return new ArrayList<>();
        }
        int index = 0;
        List<Resource> resources = new ArrayList<>();
        for (Individual individualEnd : individualEnds) {
            ObjectProperty property = MODEL.createObjectProperty(semanticConfig.getPizzaNs() + objectPropertyNames.get(index));
            Resource resource = individualStart.addProperty(property, individualEnd);
            resources.add(resource);
            ++index;
        }
        return resources;
    }

    @Override
    public boolean addDict(String d){
        System.out.println("写自定义");
        /*去除空格*/
        d=d.replaceAll(" ","_");
        File dictFile=new File(semanticConfig.getDictPath());
        FileWriter dictWriter=null;
        try{
            dictWriter=new FileWriter(dictFile,true);
            if(!dict.contains(d)){
                dict.add(d);
                dictWriter.write(d+" n 1\n");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if(dictWriter!=null){
                try {
                    dictWriter.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        return true;
    }

    @Override
    public boolean addDicts(List<String> ds){
        System.out.println("写自定义s");
        File dictFile=new File(semanticConfig.getDictPath());
        FileWriter dictWriter=null;
        try{
            dictWriter=new FileWriter(dictFile,true);
            for(String d:ds){
                /*去除空格*/
                d=d.replaceAll(" ","_");
                if(!dict.contains(d)){
                    dict.add(d);
                    dictWriter.write(d+" n 1\n");
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if(dictWriter!=null){
                try {
                    dictWriter.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    @Override
    public boolean addAlias(String origin,List<String> a){
        System.out.println("add alias!");
        File aliasFile=new File(semanticConfig.getConstruct_aliasPath());
        FileWriter aliasWriter=null;
        try{
            aliasWriter=new FileWriter(aliasFile,true);
            for(String s:a){
                /*去除空格*/
                s=s.replaceAll(" ","_");
                if(!alias.containsKey(s)&&s.length()>0){
                    alias.put(s,origin);
                    aliasWriter.write(s+" "+origin+"\n");
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if(aliasWriter!=null){
                try {
                    aliasWriter.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return true;
    }
}