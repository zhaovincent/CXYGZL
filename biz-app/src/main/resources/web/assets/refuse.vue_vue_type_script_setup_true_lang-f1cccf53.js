import{d as g,r as n,a as h,c as y,a5 as u,w as p,k as B,a3 as c,f as _,as as k,aA as D,aB as C}from"./index-e01dade3.js";import{E as I}from"./el-dialog-1471a1af.js";import{d as L}from"./index-3a646217.js";const N={class:"dialog-footer"},A=g({__name:"refuse",emits:["taskSubmitEvent"],setup(F,{expose:V,emit:x}){const t=n(!1),s=n(""),d=n(),m=n();V({handle:(r,e)=>{d.value=r,m.value=e,t.value=!0}});const w=()=>{let r=m.value;var e={};for(var a of r)if(e[a.id]=a.props.value,a.type==="Layout"){let f=a.props.value;var l=[];for(var i of f){var o={};for(var v of i){let b=v.props.value;o[v.id]=b}l.push(o)}e[a.id]=l}e[d.value.nodeId+"_approve_condition"]=!1;var E={paramMap:e,taskId:d.value.taskId,taskLocalParamMap:{approveDesc:s.value}};L(E).then(f=>{t.value=!1,x("taskSubmitEvent")})};return(r,e)=>{const a=D,l=C,i=I;return h(),y("div",null,[u(i,{modelValue:_(t),"onUpdate:modelValue":e[2]||(e[2]=o=>k(t)?t.value=o:null),title:"拒绝审核",width:"400px"},{footer:p(()=>[B("span",N,[u(l,{onClick:e[1]||(e[1]=o=>t.value=!1)},{default:p(()=>[c("取消")]),_:1}),u(l,{type:"primary",onClick:w},{default:p(()=>[c(" 确定 ")]),_:1})])]),default:p(()=>[u(a,{modelValue:_(s),"onUpdate:modelValue":e[0]||(e[0]=o=>k(s)?s.value=o:null),type:"textarea",maxlength:"100",rows:5,placeholder:"审核意见","show-word-limit":""},null,8,["modelValue"])]),_:1},8,["modelValue"])])}}});export{A as _};
