import m from"./taskUi-5dcac27c.js";import{q as d}from"./index-8f215b9b.js";import{d as v,r as n,h as i,o as p,a as k,c as f,a5 as I}from"./index-d5547550.js";const x=v({__name:"task",emits:["taskSubmitEvent"],setup(_,{expose:o,emit:l}){const a=n({}),u=t=>{a.value.taskId=t,d(t,!1).then(r=>{let e=r.data;a.value.processInstanceId=e.processInstanceId,a.value.flowId=e.flowId,a.value.processName=e.processName,a.value.nodeName=e.nodeName,a.value.nodeId=e.nodeId,a.value.taskExist=e.taskExist,a.value.starterAvatarUrl=e.starterAvatarUrl,a.value.starterName=e.starterName,a.value.startTime=e.startTime,a.value.processInstanceResult=e.processInstanceResult,s.value.deal(t,a.value,e.subProcessStarterTask,e.taskExist,e.flowId,e.formItems,e.node,e.process,e.frontJoinTask)})};i(),o({deal:u});const c=()=>{l("taskSubmitEvent")};p(()=>{});const s=n();return(t,r)=>(k(),f("div",null,[I(m,{onTaskSubmitEvent:c,ref_key:"taskUiHandler",ref:s},null,512)]))}});export{x as _};