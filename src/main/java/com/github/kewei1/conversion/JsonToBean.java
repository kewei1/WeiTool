package com.github.kewei1.conversion;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class JsonToBean {

    public static String INTEGER = "java.lang.Integer";
    public static String LONG = "java.lang.Long";
    public static String STRING = "java.lang.String";
    public static String JSONOBJECT = "com.alibaba.fastjson.JSONObject";
    public static String JSONArray = "com.alibaba.fastjson.JSONArray";
    public static String FLOAT = "java.lang.Float";
    public static String DOUBLE = "java.lang.Double";
    public static String BIG_DECIMAL = "java.math.BigDecimal";
    public static String DATE = "java.util.Date";





    /**
     * 建表语句
     *
     * @author sql
     * @date 10/23/17 3:43 PM
     */
    private static String createTable(String tableName, List<JsonMetaNode> jsonMetaNodeList) {

        String sqlCreate = "CREATE TABLE " + tableName + "(\n" + getRowName(jsonMetaNodeList);
        return sqlCreate;
    }

    private static String getRowName(List<JsonMetaNode> jsonMetaNodeList) {
        StringBuffer sqlRowNameBuffer = new StringBuffer();
        boolean hasId = false;
        for (JsonMetaNode jsonMetaNode : jsonMetaNodeList) {
            String key = jsonMetaNode.getKey();
            String valueType = jsonMetaNode.getValueType();
            String type = "";
            if (INTEGER.equals(valueType)) {
                type = "int(100)";
            } else if (LONG.equals(valueType)) {
                type = "bigint(100)";
            } else if (STRING.equals(valueType)) {
                type = "varchar(100)";
            } else if (BIG_DECIMAL.equals(valueType)) {
                type = "decimal(18,8)";
            } else if (FLOAT.equals(valueType)) {
                type = "float(100,10)";
            } else if (DOUBLE.equals(valueType)) {
                type = "double(100,10)";
            } else if (DATE.equals(valueType)) {
                type = "datetime";
            } else if (JSONOBJECT.equals(valueType) || JSONArray.equals(valueType)) {
                type = "json";
            }else {
                type = "varchar(100)";
            }
            sqlRowNameBuffer.append(key).append(" ").append(type).append(" ").append("NULL");
            if (key.equals("id")){
                hasId = true;
                sqlRowNameBuffer.append(" ").append("NOT NULL ,").append("\r\n");
            }else {
                sqlRowNameBuffer.append(",").append("\r\n");
            }
        }
        if (!hasId){
            sqlRowNameBuffer.append("id").append(" ").append("int(12)").append(" ").append(" ")
                    .append("NOT NULL ,").append("\r\n PRIMARY KEY (`id`) ");
        }
        sqlRowNameBuffer.deleteCharAt(sqlRowNameBuffer.length() - 1);
        sqlRowNameBuffer.append(" ) ENGINE=InnoDB;");
        String sqlRowName = sqlRowNameBuffer.toString();
        return sqlRowName;
    }


    public static String jsontosql(String tableName, String json)  {
        List<JsonMetaNode> jsonMetaNodeList = new ArrayList<JsonMetaNode>();
        // 转换成json对象
        JSONObject jsonObject = (JSONObject) JSON.parse(json);
        Set<String> strings = jsonObject.keySet();
        Iterator<String> iterator = strings.iterator();
        // 遍历json对象，根据key获取value并获取value的类型
        while (iterator.hasNext()) {
            JsonMetaNode jsonMete = new JsonMetaNode();
            String next = iterator.next();
            jsonMete.setKey(next);
            Object o = jsonObject.get(next);
            if (o != null) {
                String name = o.getClass().getName();
                jsonMete.setValueType(name);
            }
            jsonMetaNodeList.add(jsonMete);
        }
        //　调用建表语句的方法
        String sqlCreateTable = createTable(tableName, jsonMetaNodeList);
        System.out.println(sqlCreateTable);
        return sqlCreateTable;
    }


    public static void main1(String[] args) {

        String s = "{\"p_id\":\"73\",\"p_title\":\"微光波炉\\/烤箱保养\",\"p_mode\":\"1\",\"p_summary\":\"健康生活\",\"p_icon\":\"1491634953186.jpeg\",\"p_imageUrl\":null,\"p_priceA\":\"80\",\"p_priceB\":null,\"p_duration\":\"60\",\"p_introduce\":\"微波炉、烤箱等长时间不清洁会造成机器内汤汁堆积，对加热食物带来种种异味，滋生细菌，直接会导致肠道疾病。我们的高温深层次消毒服务，将微波炉进行深度清洁，去异味，高温杀菌，保障健康生活，延长微波炉使用寿命。\",\"p_fit_people\":\"微波炉\\/烤箱\",\"p_service_introduce\":\"\",\"p_pubtime\":\"1491634954\",\"p_pv\":\"1927\",\"c_id\":\"30\",\"m_id\":\"100\"}";
        String yy = "{\n" +
                "\t\"animals\":{\n" +
                "\t\"dog\":[\n" +
                "\t\t{\"name\":\"Rufus\",\"breed\":\"labrador\",\"count\":1,\"twoFeet\":false},\n" +
                "\t\t{\"name\":\"Marty\",\"breed\":\"whippet\",\"count\":1,\"twoFeet\":false}\n" +
                "\t],\n" +
                "\t\"cat\":{\"name\":\"Matilda\"}\n" +
                "}\n" +
                "}";

        String json = "{\"APP_HEAD\": {\"TOTAL_NUM\": \"-1\",\"PGUP_OR_PGDN\": \"0\"},\"SYS_HEAD\": {\"RET\": [{\"RET_CODE\": \"000000\",\"RET_MSG\": \"000000 SUCCESS\"},{\"RET_CODE\": \"000001\",\"RET_MSG\": \"000001 SUCCESS\"}],\"AUTH_USER_ID\": null,\"RUN_DATE\": \"20211222\",\"MESSAGE_CODE\": \"369285\",\"SOURCE_BRANCH_NO\": \"142857\"},\"BODY\": [{\"createDate\":\"20220630\",\"fileName\":\"RB_TRAN_HIST_20220630_2_1.txt\",\"filePath\":\"/acc/abcd/20220630\",\"fileScence\":\"abcd\",\"fileType\":\"txt\",\"systemId\":\"ACC\"},{\"createDate\":\"20220630\",\"fileName\":\"RB_TRAN_HIST_20220630_2_2.txt\",\"filePath\":\"/acc/abcd/20220630\",\"fileScence\":\"abcd\",\"fileType\":\"txt\",\"systemId\":\"ACC\"}]}";
        System.out.println(format(json));
        // create table
        String jsontosql = jsontosql("test", format(json));


    }


        public static String format(String jsonStr) {
            int level = 0;
            StringBuffer jsonForMatStr = new StringBuffer();
            for (int i = 0; i < jsonStr.length(); i++) {
                char c = jsonStr.charAt(i);
                if (level > 0 && '\n' == jsonForMatStr.charAt(jsonForMatStr.length() - 1)) {
                    jsonForMatStr.append(getLevelStr(level));
                }
                switch (c) {
                    case '{':
                    case '[':
                        jsonForMatStr.append(c).append("\n");
                        level++;
                        break;
                    case ',':
                        jsonForMatStr.append(c).append("\n");
                        break;
                    case '}':
                    case ']':
                        jsonForMatStr.append("\n");
                        level--;
                        jsonForMatStr.append(getLevelStr(level));
                        jsonForMatStr.append(c);
                        break;
                    default:
                        jsonForMatStr.append(c);
                        break;
                }
            }

            return jsonForMatStr.toString();

        }

        private static String getLevelStr(int level) {
            StringBuffer levelStr = new StringBuffer();
            for (int levelI = 0; levelI < level; levelI++) {
                levelStr.append("\t");
            }
            return levelStr.toString();
        }


    }




