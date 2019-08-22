package com.juma.vms.manage;

import com.bruce.tool.rpc.http.core.Https;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.juma.vms.manage.base.ListResult;
import com.juma.vms.manage.base.PageResult;
import com.juma.vms.manage.base.Result;
import org.junit.Test;
import org.springframework.util.CollectionUtils;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * 运力输送,接口测试类
 * 功能 :
 * 1.仅仅测试参数是否能正常获取.
 * 2.如果能连本地dubbo,可以尝试,manage访问本地service(目前未实现).
 * @author : Bruce(刘正航) 17:48 2019-03-29
 */
public class TransportCapacityApiTest {

//    private static final String baseUrl = "http://127.0.0.1:8080";
    private static final String baseUrl = "http://10.101.0.105:8088";
    private static final Map<String,Object> headers = Maps.newHashMap();
    static{
        // 此处的信息, 通过浏览器登录之后, 从浏览器Cookie中获取
        headers.put("Cookie","GSESSIONID=6D3604DCB57943C8AAD3D17B203B2BD4; userId=1; JSESSIONID=0AE05C5335FD5DC2C0B9FD2C52B20FE9.s1");
        headers.put("Content-Type","application/json");
    }

    @Test
    public void should_return_driveLicenseInfo_face_with_image(){
        String result = Https.create().url(baseUrl+"/transportReceive/ocrDriveLicence.html")
                .print(true)
                .addAllHeaders(headers)
                .addBody("imageUrl","http://juma-tgm.oss-cn-shenzhen.aliyuncs.com/jumaOss/xq-1553842138734.jpg")
                .addBody("faceback","face")
                .postJson();
        Result listResult1 = new Gson().fromJson(result, Result.class);
        assertNotNull(listResult1);
        assertTrue(listResult1.getCode() == 0);
    }

    @Test
    public void should_return_driveLicenseInfo_back_with_image(){
        String result = Https.create().url(baseUrl+"/transportReceive/ocrIdentification.html")
                .print(true)
                .addAllHeaders(headers)
                .addBody("imageUrl","http://juma-tgm.oss-cn-shenzhen.aliyuncs.com/jumaOss/xq-1553842138734.jpg")
                .addBody("faceback","back")
                .postJson();
        Result listResult1 = new Gson().fromJson(result, Result.class);
        assertNotNull(listResult1);
        assertEquals(Integer.valueOf(0), listResult1.getCode());
    }

    @Test
    public void should_return_identificationInfo_face_with_image(){
        String result = Https.create().url(baseUrl+"/transportReceive/ocrDriveLicence.html")
                .print(true)
                .addAllHeaders(headers)
                .addBody("imageUrl","http://juma-tgm.oss-cn-shenzhen.aliyuncs.com/jumaOss/xq-1553842138734.jpg")
                .addBody("faceback","face")
                .postJson();
        Result listResult1 = new Gson().fromJson(result, Result.class);
        assertNotNull(listResult1);
        assertEquals(Integer.valueOf(0), listResult1.getCode());
    }

    @Test
    public void should_return_identificationInfo_back_with_image(){
        String result = Https.create().url(baseUrl+"/transportReceive/ocrDriveLicence.html")
                .print(true)
                .addAllHeaders(headers)
                .addBody("imageUrl","http://juma-tgm.oss-cn-shenzhen.aliyuncs.com/jumaOss/xq-1553842138734.jpg")
                .addBody("faceback","back")
                .postJson();
        Result listResult1 = new Gson().fromJson(result, Result.class);
        assertNotNull(listResult1);
        assertEquals(Integer.valueOf(0), listResult1.getCode());
    }

    /**
     * 根据客户名称或者客户证件号 查询客户列表
     */
    @Test
    public void should_return_customer_list_with_customer_name() {
        String result = Https.create().url(baseUrl+"/transportReceive/crmCustomers.html")
                .print(true)
                .addAllHeaders(headers)
                .add("crmCustomerName","")//客户名称...
                .get();
        ListResult listResult1 = new Gson().fromJson(result, ListResult.class);
        assertNotNull(listResult1);
        assertEquals(Integer.valueOf(0), listResult1.getCode());
        assertTrue(listResult1.getData().size() <= 10);
    }

    @Test
    public void should_return_customer_list_with_customer_idcard() {
        String result = Https.create().url(baseUrl+"/transportReceive/crmCustomers.html")
                .print(true)
                .addAllHeaders(headers)
                .add("crmIdentityCardNo","")//客户身份证号...
                .get();
        ListResult listResult1 = new Gson().fromJson(result, ListResult.class);
        assertNotNull(listResult1);
        assertEquals(Integer.valueOf(0), listResult1.getCode());
        assertTrue(listResult1.getData().size() <= 10);
    }

    @Test
    public void should_return_from_department_list_with_department_name() {
        String result = Https.create().url(baseUrl+"/transportReceive/fromDepartments.html")
                .print(true)
                .addAllHeaders(headers)
                .add("deliveryDepartmentName","")//输送部门们称
                .get();
        ListResult listResult1 = new Gson().fromJson(result, ListResult.class);
        assertNotNull(listResult1);
        assertEquals(Integer.valueOf(0), listResult1.getCode());
        assertTrue(listResult1.getData().size() <= 10);
    }

    @Test
    public void should_return_to_department_list_with_department_name() {
        String result = Https.create().url(baseUrl+"/transportReceive/toDepartments.html")
                .print(true)
                .addAllHeaders(headers)
                .add("receivingDepartmentName","")//接收部门们称
                .get();
        ListResult listResult1 = new Gson().fromJson(result, ListResult.class);
        assertNotNull(listResult1);
        assertEquals(Integer.valueOf(0), listResult1.getCode());
        assertTrue(listResult1.getData().size() <= 10);
    }

    @Test
    public void should_return_transport_capacity_list_with_search_params(){
        Map<String,Object> filters = Maps.newHashMap();
        filters.put("plateNumber","川A7A88Y");//车牌号
        filters.put("truckIdentificationNo","LJ11KBBC4G8019630");//车架号
        filters.put("crmCustomerId",1);//客户ID
        filters.put("fromDepartmentId",3041);//输送部门名称
        filters.put("toDepartmentId",1464);//接收部门名称
        filters.put("receiveStatus",1);//接收状态:1已接收,0接收
        Map<String,Object> params = Maps.newHashMap();
        params.put("pageNo",1);
        params.put("pageSize",10);
        params.put("filters",filters);
        String result = Https.create().url(baseUrl+"/transportReceive/search.html")
                .print(true)
                .addAllHeaders(headers)
                .addAllBodys(params)
                .postJson();
        PageResult result1 = new Gson().fromJson(result, PageResult.class);
        assertNotNull(result1);
        assertEquals(Integer.valueOf(0),result1.getCode());
        if(!CollectionUtils.isEmpty(result1.getData().getResults())){
            assertTrue(result1.getData().getResults().size() <= 10);
        }
    }

    /**运力详情**/
    @Test
    public void should_return_transport_capacity_detail_with_item_id() {
        String result = Https.create().url(baseUrl+"/transportReceive/capacityItemDetail.html")
                .print(true)
                .addAllHeaders(headers)
                .add("itemId", 1)//列表主键ID
                .get();
        Result result1 = new Gson().fromJson(result, Result.class);
        assertNotNull(result1);
        assertEquals(Integer.valueOf(0), result1.getCode());
    }

    /**接收运力**/
    @Test
    public void should_return_transport_pre_info_with_item_id(){
        String result = Https.create().url(baseUrl+"/transportReceive/receiveTransport.html")
                .print(true)
                .addAllHeaders(headers)
                .add("itemId", 2)//列表主键ID
                .get();
        Result result1 = new Gson().fromJson(result, Result.class);
        assertNotNull(result1);
        assertEquals(Integer.valueOf(0), result1.getCode());
    }

    /**完善运力**/
    @Test
    public void should_do_complete_transport_info_by_vendor_truck_and_driver(){
        String result = Https.create().url(baseUrl+"/transportReceive/completeTransport.html")
                .print(true)
                .addAllHeaders(headers)
                .addBody("vendorId", 1)//承运商ID
                .addBody("truckId", 1)//卡车ID
                .addBody("driverId", 1)//司机ID
                .postJson();
        Result result1 = new Gson().fromJson(result, Result.class);
        assertNotNull(result1);
        assertEquals(Integer.valueOf(0), result1.getCode());
    }

    /**新建承运商:前置信息获取**/
    @Test
    public void should_return_vendor_pre_info_by_crm_customer_id(){
        String result = Https.create().url(baseUrl+"/transportReceive/beforeAddVendor.html")
                .print(true)
                .addAllHeaders(headers)
                .add("crmCustomerId", 1)//crm客户ID
                .get();
        Result result1 = new Gson().fromJson(result, Result.class);
        assertNotNull(result1);
        assertEquals(Integer.valueOf(0), result1.getCode());
    }
}
