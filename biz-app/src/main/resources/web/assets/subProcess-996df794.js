import{d as O,h as v,r as N,o as V,D as u,L as E,ck as L,a as d,c as r,k as a,au as T,f as i,P as z,cl as J,q as b,am as M,n as U,a5 as j,ag as q,ah as F}from"./index-d5547550.js";import{p as H,b as K}from"./const-a11e1fef.js";import A from"./addNode-8159b811.js";import{$ as h}from"./index-a7c7b111.js";import{u as G}from"./index-71f0fd6b.js";import{_ as Q}from"./_plugin-vue_export-helper-c27b6911.js";import"./el-popper-00977718.js";import"./el-popover-07a17928.js";import"./dropdown-b09318e3.js";import"./flow-165b4256.js";const y=e=>(q("data-v-5869021c"),e=e(),F(),e),R={class:"node-wrap"},W=["placeholder"],X={class:"text"},Y=y(()=>a("i",{class:"anticon anticon-right arrow"},null,-1)),Z={key:0,class:"error_tip"},ee=y(()=>a("i",{class:"anticon anticon-exclamation-circle"},null,-1)),oe=[ee],te=O({__name:"subProcess",props:{nodeConfig:{type:Object,default:()=>{}}},emits:["updateData"],setup(e,{emit:P}){const n=e;v();let l=N(!1);V(()=>{n.nodeConfig.error=!h.subProcessOK(n.nodeConfig)});const k=o=>{l.value=!1,n.nodeConfig.nodeName=n.nodeConfig.nodeName||c};let c=u(()=>H[n.nodeConfig.type]);var f=u(()=>h.subProcessStr(n.nodeConfig));const S=o=>{o||o===0?D.value[o]=!0:l.value=!0};let D=N([]);const g=o=>{P("updateData",o)},x=()=>{g(n.nodeConfig.childNode)};let m=G(),{setSubProcess:_,setSubProcessConfig:w}=m,p=v().uid,I=u(()=>m.subProcessConfigData);E(I,o=>{o.flag&&o.id===p&&g(o.value)});const $=()=>{_(!0),w({value:JSON.parse(JSON.stringify(n.nodeConfig)),flag:!1,id:p})};return(o,t)=>{var C;const B=L("focus");return d(),r("div",R,[a("div",{class:U(["node-wrap-box",(e.nodeConfig.type==0?"start-node ":"")+(e.nodeConfig.error?"active error":"")])},[a("div",{class:"title",style:T(`background: rgb(${i(K)[e.nodeConfig.type]});`)},[i(l)?z((d(),r("input",{key:0,type:"text",class:"ant-input editable-title-input",onBlur:t[0]||(t[0]=s=>k()),onFocus:t[1]||(t[1]=s=>s.currentTarget.select()),"onUpdate:modelValue":t[2]||(t[2]=s=>e.nodeConfig.nodeName=s),placeholder:i(c)},null,40,W)),[[B],[J,e.nodeConfig.nodeName]]):(d(),r("span",{key:1,class:"editable-title",onClick:t[3]||(t[3]=s=>S())},b(e.nodeConfig.nodeName),1)),a("i",{class:"anticon anticon-close close",onClick:x})],4),a("div",{class:"content",onClick:$},[a("div",X,b(((C=i(f))==null?void 0:C.length)>0?i(f):"请选择"+i(c)),1),Y]),e.nodeConfig.error?(d(),r("div",Z,oe)):M("",!0)],2),j(A,{"current-node":e.nodeConfig,childNodeP:e.nodeConfig.childNode,"onUpdate:childNodeP":t[4]||(t[4]=s=>e.nodeConfig.childNode=s)},null,8,["current-node","childNodeP"])])}}});const ge=Q(te,[["__scopeId","data-v-5869021c"]]);export{ge as default};
