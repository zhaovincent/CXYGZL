import{aH as t}from"./index-d5547550.js";function r(e){return t({url:"/process/create",method:"post",data:e})}function s(e){return t({url:"/process/getDetail?flowId="+e,method:"get"})}function u(e){return t({url:"/process/update/"+e+"?type=stop",method:"put"})}function a(e){return t({url:"/process/update/"+e+"?type=delete",method:"put"})}function n(e){return t({url:"/process/update/"+e+"?type=using",method:"put"})}function p(e){return t({url:"/process-instance/startProcessInstance",method:"post",data:e})}export{r as a,a as b,u as d,n as e,s as g,p as s};