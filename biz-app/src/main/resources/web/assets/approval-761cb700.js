import{d as S,h as w,r as v,D as N,ca as I,a as s,c as d,k as a,au as D,f as r,q as l,R as $,P as B,cA as P,am as T,n as V,a5 as E,ag as L,ah as U}from"./index-e01dade3.js";import{p as z,b as A}from"./const-36faa2f4.js";import F from"./addNode-6db4a717.js";import{$ as u}from"./index-126d0406.js";/* empty css                                                                 */import{_ as j}from"./_plugin-vue_export-helper-c27b6911.js";import"./el-popper-ee5283d6.js";import"./el-popover-70136418.js";import"./dropdown-b5481c97.js";import"./flow-2349aa77.js";const p=e=>(L("data-v-54e926db"),e=e(),U(),e),q={class:"node-wrap"},x={key:0},H=p(()=>a("span",{class:"iconfont"},l(""),-1)),M=["placeholder"],O={class:"text"},R=p(()=>a("i",{class:"anticon anticon-right arrow"},null,-1)),G={key:0,class:"error_tip"},J=p(()=>a("i",{class:"anticon anticon-exclamation-circle"},null,-1)),K=[J],Q=S({__name:"approval",props:{nodeConfig:{type:Object,default:()=>{}}},setup(e){const o=e;w();let c=v(!1);const y=n=>{n||n===0?(m.value[n]=!1,o.nodeConfig.conditionNodes[n].nodeName=o.nodeConfig.conditionNodes[n].nodeName||"条件"):(c.value=!1,o.nodeConfig.nodeName=o.nodeConfig.nodeName||f)};let f=N(()=>z[o.nodeConfig.type]);var g=N(()=>o.nodeConfig.type==0?u.arrToStr(o.nodeConfig.nodeUserList)||"所有人":o.nodeConfig.type==1?u.setApproverStr(o.nodeConfig):o.nodeConfig.type==2?u.copyerStr(o.nodeConfig):"");const h=n=>{n||n===0?m.value[n]=!0:c.value=!0};let m=v([]);const k=()=>{emits("update:nodeConfig",o.nodeConfig.childNode)};return(n,t)=>{var C;const b=I("focus");return s(),d("div",q,[a("div",{class:V(["node-wrap-box",(e.nodeConfig.type==0?"start-node ":"")+(e.nodeConfig.error?"active error":"")])},[a("div",{class:"title",style:D(`background: rgb(${r(A)[e.nodeConfig.type]});`)},[e.nodeConfig.type==0?(s(),d("span",x,l(e.nodeConfig.nodeName),1)):(s(),d($,{key:1},[H,r(c)?B((s(),d("input",{key:0,type:"text",class:"ant-input editable-title-input",onBlur:t[0]||(t[0]=i=>y()),onFocus:t[1]||(t[1]=i=>i.currentTarget.select()),"onUpdate:modelValue":t[2]||(t[2]=i=>e.nodeConfig.nodeName=i),placeholder:r(f)},null,40,M)),[[b],[P,e.nodeConfig.nodeName]]):(s(),d("span",{key:1,class:"editable-title",onClick:t[3]||(t[3]=i=>h())},l(e.nodeConfig.nodeName),1)),a("i",{class:"anticon anticon-close close",onClick:k})],64))],4),a("div",{class:"content",onClick:t[4]||(t[4]=(...i)=>n.openConfigDrawer&&n.openConfigDrawer(...i))},[a("div",O,l(((C=r(g))==null?void 0:C.length)>0?r(g):"请选择"+r(f)),1),R]),e.nodeConfig.error?(s(),d("div",G,K)):T("",!0)],2),E(F,{"current-node":e.nodeConfig,childNodeP:e.nodeConfig.childNode,"onUpdate:childNodeP":t[5]||(t[5]=i=>e.nodeConfig.childNode=i)},null,8,["current-node","childNodeP"])])}}}),ae=j(Q,[["__scopeId","data-v-54e926db"]]);export{ae as default};