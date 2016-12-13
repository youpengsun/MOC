package com.sap.moc.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.sap.moc.vo.PasArticle;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
/**
 * @author I074115
 *
 */
public class MessageUtil {
	/*Wechat Message Type                            */
    //Message Text
    public static final String REQ_MESSAGE_TYPE_TEXT = "text";
    //Message Image
    public static final String REQ_MESSAGE_TYPE_IMAGE = "image";
    //Message voice
    public static final String REQ_MESSAGE_TYPE_VOICE = "voice";  
    //Message video
    public static final String REQ_MESSAGE_TYPE_VIDEO = "video";
    //Message shortvideo
    public static final String REQ_MESSAGE_TYPE_SHORTVIDEO = "shortvideo";
    //Message location
    public static final String REQ_MESSAGE_TYPE_LOCATION = "location";
    //Message Link
    public static final String REQ_MESSAGE_TYPE_LINK = "link";
    //Message Event
    public static final String REQ_MESSAGE_TYPE_EVENT = "event";
    
    /*Wechat Event Type                            */
    //Message subscribe
    public static final String EVENT_TYPE_SUBSCRIBE = "subscribe";
    //Message Unsubscribe 
    public static final String EVENT_TYPE_UNSUBSCRIBE = "unsubscribe";
    //Message CLICK
    public static final String EVENT_TYPE_CLICK = "click";
    //Message VIEW
    public static final String EVENT_TYPE_VIEW = "VIEW";
    //Message scancode_push
    public static final String EVENT_TYPE_SCANCODE_PUSH = "scancode_push";
    //Message scancode_waitmsg
    public static final String EVENT_TYPE_SCANCODE_WAIT = "scancode_waitmsg";
    //Message pic_sysphoto
    public static final String EVENT_TYPE_SCANCODE_SYSPHOTO = "pic_sysphoto";
    //Message location_select
    public static final String EVENT_TYPE_SCANCODE_LOCSELECT = "location_select";
    //Batch job result
    public static final String EVENT_TYPE_BATCH_JOB_RESULT = "batch_job_result";
    
    /*Wechat Event Key                            */
    //Consume Meal
    public static final String EVENT_SCANCODE_CONSUME_MEAL = "consume";
    //Vendor daily count
    public static final String EVENT_VENDOR_DAILY_COUNT = "dailycount";
    
    /**
     * Parse XML
     * @param request
     * @return
     * @throws Exception
     */
    public static Map<String, Object> parseXml(String msg)
            throws Exception {
        // Parse XML into HashMap
        Map<String, Object> map = new HashMap<String, Object>();
        // Get byte stream from msg
        InputStream inputStream = new ByteArrayInputStream(msg.getBytes("UTF-8"));
        // SAX Reader Instance
        SAXReader reader = new SAXReader();
        Document document = reader.read(inputStream);        
        // Get XML root element
        Element root = document.getRootElement(); 
        // Parse XML with recursion in case multiple levels
        recurseParseXML(root,map);

        // Release
        inputStream.close();
        inputStream = null;
        return map;
    }

    /**
     * Parse XML with recursion
     * @param root
     * @return Map
     */
    @SuppressWarnings("unchecked")
    public static void recurseParseXML(Element root,Map<String,Object> map){
    	for(Element e : (List<Element>)root.elements())
    	 if(e.isTextOnly()){
       	  map.put(e.getName(), e.getText());
         }else{
          Map<String,Object> tempmap = new HashMap<String,Object>();
          recurseParseXML(e,tempmap);
          map.put(e.getName(), tempmap);
         } 	
    }
    
    /**
     * Text Message converted into XML
     * @param textMessage
     * @return xml
     */
    public static <T> String textMessageToXml(T Message) {
        xstream.alias("xml", Message.getClass());
        xstream.alias("item", PasArticle.class);
        return xstream.toXML(Message);
    }
 

    /**
     * Enhance xstream to enable CDATA
     * @date 2013-05-19
     */
    private static XStream xstream = new XStream(new XppDriver() {
        @Override
        public HierarchicalStreamWriter createWriter(Writer out) {
            return new PrettyPrintWriter(out) {
                // 对所有xml节点的转换都增加CDATA标记
                boolean cdata = true;

                @Override
                @SuppressWarnings("rawtypes")
                public void startNode(String name, Class clazz) {
                    super.startNode(name, clazz);
                }

                @Override
                protected void writeText(QuickWriter writer, String text) {
                    if (cdata) {
                        writer.write("<![CDATA[");
                        writer.write(text);
                        writer.write("]]>");
                    } else {
                        writer.write(text);
                    }
                }
            };
        }
    });
}
