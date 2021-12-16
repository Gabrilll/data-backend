package com.judicature.databackend.blImpl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.judicature.databackend.bl.FileService;
import com.judicature.databackend.data.NodeRepository;
import com.judicature.databackend.data.RelationRepository;
import com.judicature.databackend.po.Node;
import com.judicature.databackend.po.Property;
import com.judicature.databackend.po.Relation;
import com.judicature.databackend.util.VO2PO;
import com.judicature.databackend.vo.Graph;
import com.judicature.databackend.vo.GraphVO;
import com.judicature.databackend.vo.NodeVO;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

@Service
public class FileServiceImpl implements FileService {
    @Autowired
    NodeRepository nodeRepository;

    @Autowired
    RelationRepository relationRepository;

    private Logger logger = Logger.getLogger(getClass().getName());



    @Override
    public int json2Dao(MultipartFile mFile) {
        try{
            // mFile to file
            String fullFileName = StringUtils.cleanPath(Objects.requireNonNull(mFile.getOriginalFilename()));
            File file = File.createTempFile(fullFileName.substring(0,fullFileName.lastIndexOf('.')), fullFileName.substring(fullFileName.lastIndexOf('.')-1));
            file.deleteOnExit();
            FileUtils.copyInputStreamToFile(mFile.getInputStream(), file);
            // file to str
            String str = FileUtils.readFileToString(file, "utf-8");
            // str to JSONObject
            System.out.println(str);

            JSONObject jsonObject = JSONObject.parseObject(str);
            // get data
            JSONArray nodesArray = jsonObject.getJSONArray("node");
            List<NodeVO> nodes = new ArrayList<>();
            JSONArray linksArray = jsonObject.getJSONArray("relationship");
            //List<Relation> relations = new ArrayList<>();

            if (nodesArray != null){

            }
            for(int i=0;i< nodesArray.size();i++){
//                System.out.println(i);
                JSONObject nodeJson = nodesArray.getJSONObject(i);
                Node node = new Node();
                List<String> labelsList = new ArrayList<>();
                JSONArray labelsJsonArray = nodeJson.getJSONArray("labels");
                // labels数组
                for(int j = 0;j < labelsJsonArray.size();j++){
                    labelsList.add(labelsJsonArray.get(j).toString());
                }
                node.setLabels(labelsList);

                // properties数组
                List<Property> properties = new ArrayList<>();
                JSONObject propertiesJson = nodeJson.getJSONObject("properties");
                System.out.println(propertiesJson);
                Map<String, String> propertyMap = JSONObject.parseObject(propertiesJson.toJSONString(),
                        new TypeReference<Map<String, String>>(){});
                for (Map.Entry entry : propertyMap.entrySet()) {
                    Property property = new Property();
                    property.setKey(entry.getKey().toString());
                    property.setValue(entry.getValue().toString());
                    properties.add(property);
                }
                node.setProperties(properties);

                nodeRepository.addNode(node);
//                nodes.add(VO2PO.toNodeVO(node));
//                Graph.addNode(VO2PO.toNodeVO(node));
                GraphVO graphVO = new GraphVO();
                graphVO.setNodes(nodes);
                Graph.setInstance(graphVO);
                Graph.addNode(VO2PO.toNodeVO(node));
                System.out.println("Node insert successfully!");

            }

            // 插入关系
            for(int i =0;i < linksArray.size();i++){
                Relation relation = new Relation();
                JSONObject relationJson = linksArray.getJSONObject(i);
                Long start_id = relationJson.getLong("start");

                Long end_id = relationJson.getLong("end");
                relation.setStart(start_id);
                relation.setEnd(end_id);

                String type = relationJson.getString("type");
                relation.setType(type);

                // 设置properties数组
                List<Property> reProperties = new ArrayList<>();
                JSONObject propertiesJson = relationJson.getJSONObject("properties");
                System.out.println(propertiesJson);
                Map<String, String> propertyMap = JSONObject.parseObject(propertiesJson.toJSONString(),
                        new TypeReference<Map<String, String>>(){});
                for (Map.Entry entry : propertyMap.entrySet()) {
                    Property property = new Property();
                    property.setKey(entry.getKey().toString());
                    property.setValue(entry.getValue().toString());
                    reProperties.add(property);
                }
                relation.setProperties(reProperties);
                Long res = relationRepository.addRelation(relation);
            }


            return 1;
        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }
}
