package com.blacksun.readxmldoom;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            ArrayList<HashMap<String, String>> userList = new ArrayList<>();
            ListView lv = (ListView) findViewById(R.id.user_list);
            InputStream istream = getAssets().open("userdetails.xml");
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = builderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(istream);
            NodeList nList = doc.getElementsByTagName("user");
            for (int i = 0; i < nList.getLength(); i++) {
                if (nList.item(0).getNodeType() == Node.ELEMENT_NODE) {
                    HashMap<String, String> user = new HashMap<>();
                    Element elm = (Element) nList.item(i);
                    user.put("name", getNodeValue("name", elm));
                    user.put("designation", getNodeValue("designation", elm));
                    user.put("location", getNodeValue("location", elm));
                    userList.add(user);
                }
            }
            ListAdapter adapter = new SimpleAdapter(MainActivity.this, userList, R.layout.list_row, new String[]{"name", "designation", "location"}, new int[]{R.id.name, R.id.designation, R.id.location});
            lv.setAdapter(adapter);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    protected String getNodeValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag);
        Node node = nodeList.item(0);
        if (node != null) {
            if (node.hasChildNodes()) {
                Node child = node.getFirstChild();
                while (child != null) {
                    if (child.getNodeType() == Node.TEXT_NODE) {
                        return child.getNodeValue();
                    }
                }
            }
        }
        return "";
    }
}