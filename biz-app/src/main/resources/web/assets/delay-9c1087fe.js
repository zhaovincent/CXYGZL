import{d as V,h as v,r as y,o as E,D as u,L,ck as O,a as l,c as d,k as s,au as T,f as i,P as z,cl as J,q as N,am as M,n as U,a5 as j,ag as q,ah as F}from"./index-d5547550.js";import{p as H,b as A}from"./const-a11e1fef.js";import G from"./addNode-8159b811.js";import{$ as K}from"./index-a7c7b111.js";import{u as Q}from"./index-71f0fd6b.js";import{_ as R}from"./_plugin-vue_export-helper-c27b6911.js";import"./el-popper-00977718.js";import"./el-popover-07a17928.js";import"./dropdown-b09318e3.js";import"./flow-165b4256.js";const h=e=>(q("data-v-587a7609"),e=e(),F(),e),W={class:"node-wrap"},X=["placeholder"],Y={class:"text"},Z=h(()=>s("i",{class:"anticon anticon-right arrow"},null,-1)),ee={key:0,class:"error_tip"},oe=h(()=>s("i",{class:"anticon anticon-exclamation-circle"},null,-1)),te=[oe],ne=V({__name:"delay",props:{nodeConfig:{type:Object,default:()=>{}}},emits:["updateData"],setup(e,{emit:k}){const n=e,{proxy:D}=v();let r=y(!1);E(()=>{n.nodeConfig.error=D.$isBlank(n.nodeConfig.value)});const b=()=>{r.value=!1,n.nodeConfig.nodeName=n.nodeConfig.nodeName||c};let c=u(()=>H[n.nodeConfig.type]);var f=u(()=>K.delayStr(n.nodeConfig));const x=o=>{o||o===0?S.value[o]=!0:r.value=!0};let S=y([]);const g=o=>{k("updateData",o)},_=()=>{g(n.nodeConfig.childNode)};let m=Q(),{setDelay:w,setDelayConfig:I}=m,p=v().uid,$=u(()=>m.delayConfigData);L($,o=>{console.log("数据变化l",o),o.flag&&o.id===p&&g(o.value)});const B=()=>{w(!0),I({value:JSON.parse(JSON.stringify(n.nodeConfig)),flag:!1,id:p})};return(o,t)=>{var C;const P=O("focus");return l(),d("div",W,[s("div",{class:U(["node-wrap-box",(e.nodeConfig.type==0?"start-node ":"")+(e.nodeConfig.error?"active error":"")])},[s("div",{class:"title",style:T(`background: rgb(${i(A)[e.nodeConfig.type]});`)},[i(r)?z((l(),d("input",{key:0,type:"text",class:"ant-input editable-title-input",onBlur:t[0]||(t[0]=a=>b()),onFocus:t[1]||(t[1]=a=>a.currentTarget.select()),"onUpdate:modelValue":t[2]||(t[2]=a=>e.nodeConfig.nodeName=a),placeholder:i(c)},null,40,X)),[[P],[J,e.nodeConfig.nodeName]]):(l(),d("span",{key:1,class:"editable-title",onClick:t[3]||(t[3]=a=>x())},N(e.nodeConfig.nodeName),1)),s("i",{class:"anticon anticon-close close",onClick:_})],4),s("div",{class:"content",onClick:B},[s("div",Y,N(((C=i(f))==null?void 0:C.length)>0?i(f):"请选择"+i(c)),1),Z]),e.nodeConfig.error?(l(),d("div",ee,te)):M("",!0)],2),j(G,{"current-node":e.nodeConfig,childNodeP:e.nodeConfig.childNode,"onUpdate:childNodeP":t[4]||(t[4]=a=>e.nodeConfig.childNode=a)},null,8,["current-node","childNodeP"])])}}});const me=R(ne,[["__scopeId","data-v-587a7609"]]);export{me as default};
