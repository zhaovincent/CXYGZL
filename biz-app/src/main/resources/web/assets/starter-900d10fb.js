import{p as D,b}from"./const-a11e1fef.js";import k from"./addNode-8159b811.js";import{$ as w}from"./index-a7c7b111.js";import{u as I}from"./index-71f0fd6b.js";import{d as P,h as f,r as B,D as r,L,a as u,c as m,k as t,au as O,f as a,q as p,am as V,n as $,a5 as z,ag as J,ah as T}from"./index-d5547550.js";import{_ as U}from"./_plugin-vue_export-helper-c27b6911.js";import"./el-popper-00977718.js";import"./el-popover-07a17928.js";import"./dropdown-b09318e3.js";import"./flow-165b4256.js";const g=e=>(J("data-v-62fd2efd"),e=e(),T(),e),j={class:"node-wrap"},q={class:"text"},E=g(()=>t("i",{class:"anticon anticon-right arrow"},null,-1)),H={key:0,class:"error_tip"},A=g(()=>t("i",{class:"anticon anticon-exclamation-circle"},null,-1)),F=[A],G=P({__name:"starter",props:{nodeConfig:{type:Object,default:()=>{}}},emits:["updateData"],setup(e,{emit:C}){const n=e;f(),B(!1);let h=r(()=>D[n.nodeConfig.type]);var s=r(()=>w.arrToStr(n.nodeConfig.nodeUserList)||"所有人");const _=o=>{C("updateData",o)};let d=I(),{setPromoter:v,setStarterConfig:N}=d,i=f().uid,y=r(()=>d.starterConfigData);L(y,o=>{o.flag&&o.id===i&&_(o.value)});const S=()=>{v(!0),N({value:JSON.parse(JSON.stringify(n.nodeConfig)),flag:!1,id:i})};return(o,c)=>{var l;return u(),m("div",j,[t("div",{class:$(["node-wrap-box",(e.nodeConfig.type==0?"start-node ":"")+(e.nodeConfig.error?"active error":"")])},[t("div",{class:"title",style:O(`background: rgb(${a(b)[e.nodeConfig.type]});`)},[t("span",null,p(e.nodeConfig.nodeName),1)],4),t("div",{class:"content",onClick:S},[t("div",q,p(((l=a(s))==null?void 0:l.length)>0?a(s):"请选择"+a(h)),1),E]),e.nodeConfig.error?(u(),m("div",H,F)):V("",!0)],2),z(k,{"current-node":e.nodeConfig,childNodeP:e.nodeConfig.childNode,"onUpdate:childNodeP":c[0]||(c[0]=x=>e.nodeConfig.childNode=x)},null,8,["current-node","childNodeP"])])}}});const oe=U(G,[["__scopeId","data-v-62fd2efd"]]);export{oe as default};
