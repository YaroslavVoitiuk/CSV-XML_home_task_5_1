package ru.netology;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main( String[] args ) throws ParserConfigurationException, IOException, SAXException {

        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String jsonCSV = listToJson(parseCSV(columnMapping));
        writeString(jsonCSV,"data.json");

        String jsonXML = listToJson(readXML());
        writeString(jsonXML,"new_data.json");

    }

    public static List<Employee> readXML() throws ParserConfigurationException, IOException, SAXException {

        List<Employee> employees = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File("data.xml"));
        Node root = document.getDocumentElement();
        NodeList nodeList = root.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node employee = nodeList.item(i);
            if(Node.TEXT_NODE != employee.getNodeType()){
                Element element = (Element) employee;
                employees.add(new Employee(Long.parseLong((element.getElementsByTagName("id").item(0)).getTextContent()),
                        (element.getElementsByTagName("firstName").item(0)).getTextContent(),
                        (element.getElementsByTagName("lastName").item(0)).getTextContent(),
                        (element.getElementsByTagName("country").item(0)).getTextContent(),
                        Integer.parseInt((element.getElementsByTagName("age").item(0)).getTextContent())));
            }
        }
        System.out.println(employees);
        return employees;
    }


    public static List<Employee> parseCSV(String [] columnMapping){
        try (CSVReader csvReader = new CSVReader(new FileReader("data.csv"))){
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);

            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(csvReader)
                    .withMappingStrategy(strategy)
                    .build();

            List<Employee> employees = csv.parse();
            return employees;

        } catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    public static String listToJson(List<Employee> employees){

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.toJson(employees);

    }

    public static void writeString(String json,String fileName){
        try {
            FileWriter writer = new FileWriter(fileName);
            writer.write(json);
            writer.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
    }




}
