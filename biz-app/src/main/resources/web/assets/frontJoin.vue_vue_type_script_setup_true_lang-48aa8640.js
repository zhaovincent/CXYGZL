import{d as D,r as o,h as L,a as h,c as B,a5 as r,w as p,k as T,a3 as c,f,as as _,a7 as U,aC as N,aD as F}from"./index-d5547550.js";/* empty css                  */import{E as J}from"./el-text-a7cd3acd.js";import{b as M}from"./index-8f215b9b.js";import{_ as S}from"./selectAndShow.vue_vue_type_script_setup_true_lang-9eafc7ba.js";import{E as $}from"./index-a6355ec2.js";const O={class:"dialog-footer"},P=D({__name:"frontJoin",emits:["taskSubmitEvent"],setup(R,{expose:V,emit:y}){const l=o(!1),i=o(""),v=o(),g=o(),x=o(""),w=o(""),k=o("");L(),V({handle:(d,e,a,s,n)=>{k.value=n,v.value=d,g.value=e,x.value=a,w.value=s,l.value=!0}});const b=()=>{if(u.value.length==0){U.warning("请选择人员");return}let d=g.value;var e={};for(var a of d)if(e[a.id]=a.props.value,a.type==="Layout"){let I=a.props.value;var s=[];for(var n of I){var m={};for(var t of n){let C=t.props.value;m[t.id]=C}s.push(m)}e[a.id]=s}var E={paramMap:e,taskId:v.value.taskId,nodeId:x.value,approveDesc:i.value,targetUserId:u.value[0].id,processInstanceId:v.value.processInstanceId};console.log(E),M(E).then(I=>{l.value=!1,y("taskSubmitEvent")})},u=o([]);return(d,e)=>{const a=J,s=N,n=F,m=$;return h(),B("div",null,[r(m,{modelValue:f(l),"onUpdate:modelValue":e[3]||(e[3]=t=>_(l)?l.value=t:null),title:f(k),width:"400px"},{footer:p(()=>[T("span",O,[r(n,{onClick:e[2]||(e[2]=t=>l.value=!1)},{default:p(()=>[c("取消")]),_:1}),r(n,{type:"primary",onClick:b},{default:p(()=>[c(" 确定 ")]),_:1})])]),default:p(()=>[r(a,{type:"primary"},{default:p(()=>[c("选择人员")]),_:1}),r(S,{orgList:f(u),"onUpdate:orgList":e[0]||(e[0]=t=>_(u)?u.value=t:null),"select-self":!1,type:"user",multiple:!1},null,8,["orgList"]),r(s,{style:{"margin-top":"20px"},modelValue:f(i),"onUpdate:modelValue":e[1]||(e[1]=t=>_(i)?i.value=t:null),type:"textarea",maxlength:"100",rows:5,placeholder:"审核意见","show-word-limit":""},null,8,["modelValue"])]),_:1},8,["modelValue","title"])])}}});export{P as _};