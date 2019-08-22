package com.juma.vms.manage;

import com.bruce.tool.rpc.http.core.Https;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.juma.vms.manage.base.ListResult;
import com.juma.vms.manage.base.PageResult;
import com.juma.vms.transport.domain.TransportTruckRefund;
import com.juma.vms.transport.enumeration.RefundReasonType;
import com.juma.vms.truck.enumeration.TruckRunTypeEnum;
import com.juma.vms.vendor.enumeration.VendorTypeEnum;
import org.junit.Test;
import org.springframework.util.CollectionUtils;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * 功能 :
 * 1.仅仅测试参数是否能正常获取.
 * 2.如果能连本地dubbo,可以尝试,manage访问本地service(目前未实现).
 * 工作流流程说明:
 * 0.准备工作:
 *      a.登陆用户中心-后台管理,进入工作流平台.
 *      b.进入流程管理-->流程种类管理.
 *      c.在页面菜单树中,合适的位置,建立树节点.
 *      d.在节点上创建"叶子分类".
 *      e.在属性中,填入"种类Key".审批地址(前端提供),详情页面地址(前端提供).
 * 1.在创建退车单的时候,后台调用工作流系统创建对应的工作流.
 *      a.此时,如果配置没问题,在工作流后台的代办任务里可以看到.
 * 2.
 * @author : Bruce(刘正航) 10:07 2019-04-08
 */
public class TransportTruckRefundApiTest {

//    private static final String baseUrl = "http://127.0.0.1:8080";
    private static final String baseUrl = "http://10.101.0.105:8088";
    private static final Map<String,Object> headers = Maps.newHashMap();
    static{
        // 此处的信息, 通过浏览器登录之后, 从浏览器Cookie中获取
        headers.put("Cookie","GSESSIONID=4B180938C1854AEFBB21CC2B03697D82; userId=1; JSESSIONID=348921037B9367FD9190C5645F922A5A.s1;");
        headers.put("Content-Type","application/json");
    }

    /**退车单列表**/
    @Test
    public void should_return_truck_refund_list_with_params(){
        Map<String,Object> filters = Maps.newHashMap();
        filters.put("departmentId",1);//接收部门名称
        Map<String,Object> params = Maps.newHashMap();
        params.put("pageNo",1);
        params.put("pageSize",10);
        params.put("filters",filters);
        String result = Https.create().url(baseUrl+"/transportRefund/search.html")
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

    @Test
    public void do_cacel_truck_refund_with_refund_id(){

    }

    @Test
    public void should_return_truck_list_with_plate_number(){
        String result = Https.create().url(baseUrl+"/transportRefund/findRefundTruckInfos.html")
                .print(true)
                .addAllHeaders(headers)
                .addBody("plateNumber","川A73460")//车牌号
                .postJson();
        ListResult listResult1 = new Gson().fromJson(result, ListResult.class);
        assertNotNull(listResult1);
        assertEquals(Integer.valueOf(0), listResult1.getCode());
        assertTrue(listResult1.getData().size() <= 10);
    }

    @Test
    public void do_add_truck_refund_with_truck_refund_info(){
        TransportTruckRefund refund = new TransportTruckRefund();
        refund.setPlateNumber("川A73460");
        refund.setTruckIdentificationNo("LJ11KBBC6G8019615");
        refund.setVehicleBoxType(31);
        refund.setVehicleBoxLength(57);
        refund.setTruckRunType(TruckRunTypeEnum.OWN_SALE.getCode());
        refund.setAreaCode("000400");
        refund.setVendorId(20);
        refund.setContactPhone("18880448113");
        refund.setVendorType(VendorTypeEnum.PERSONAL.getCode());
        refund.setDepartmentId(1479);
        refund.setRefundAttachments("[]");
        refund.setRefundReasonType(RefundReasonType.DRIVER.getCode().byteValue());
        refund.setRefundReason("测试退车");

        Gson gson = new Gson();
        String mapStr = gson.toJson(refund);
        Map<String,Object> params = gson.fromJson(mapStr,Map.class);

        String result = Https.create().url(baseUrl+"/transportRefund/addTransportTruckRefund.html")
                .print(true)
                .addAllHeaders(headers)
                .addAllBodys(params)
                .postJson();
        ListResult listResult1 = new Gson().fromJson(result, ListResult.class);
        assertNotNull(listResult1);
        assertEquals(Integer.valueOf(0), listResult1.getCode());
        assertTrue(listResult1.getData().size() <= 10);
    }

    @Test
    public void should_return_truck_refund_detail_with_refund_id(){
    }

    @Test
    public void should_return_task_detail_with_task_id(){
    }

    @Test
    public void should_return_task_detail_with_process_id(){
    }

    @Test
    public void should_return_refund_list_with_search_params(){
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
}
