import{d as j,r as G,D as g,L as H,h as J,K as Q,a as o,l as s,w as a,a5 as e,c as _,R as v,a6 as V,f as h,a3 as $,as as X,k as L,aC as Y,aD as Z}from"./index-d5547550.js";import{E as ee}from"./el-drawer-75c80c60.js";/* empty css                */import{a as le,E as ae}from"./el-row-a594fbeb.js";import"./el-tag-de6752b8.js";import{E as te,a as oe}from"./el-select-46753b34.js";import"./el-scrollbar-cbe0c54f.js";import"./el-popper-00977718.js";import{E as re}from"./el-switch-a81630c6.js";import{a as ue,E as ne}from"./el-form-item-2de9c3c8.js";import{E as de}from"./el-alert-867b40be.js";import{u as se}from"./index-71f0fd6b.js";import{u as pe}from"./flow-165b4256.js";import{$ as ie}from"./index-a7c7b111.js";const me=L("p",null,"仅支持POST请求，以请求体方式接收参数",-1),fe=L("p",null,"通过接口可以修改表单值：左侧表单表示要修改的表单值，右侧字段是指接口返回的字段名",-1),$e=j({__name:"triggerDrawer",setup(ce){let y=G({});const p=g(()=>k.value.value.httpSetting);let w=se(),{setTriggerConfig:T,setTrigger:B}=w,O=g(()=>w.triggerDrawer),k=g(()=>w.triggerConfigData),b=g({get(){return O.value},set(){M()}});H(k,r=>{y.value=r.value});const{proxy:z}=J();let P=pe();const R=Q({url:[{required:!0,message:"请填写请求地址",type:"url",trigger:"blur"}]});var D=g(()=>P.step2.filter(r=>r.type!="Description")),S=g(()=>{let r=z.$deepCopy(D.value);return r.push({id:"root",type:"SelectUser",name:"发起人"}),r});const C=(r,u)=>{p.value[u].push({field:"",valueMode:!0,value:""})},U=(r,u,m)=>{p.value[u].splice(m,1)},q=(r,u,m)=>{p.value[u][m].value=""},I=()=>{},A=()=>{y.value.error=!ie.checkTrigger(y.value),T({value:y.value,flag:!0,id:k.value.id}),M()},M=()=>{B(!1)};return(r,u)=>{const m=de,f=Y,i=ue,d=le,F=re,x=te,E=oe,c=Z,N=ae,K=ne,W=ee;return o(),s(W,{"append-to-body":!0,title:"触发器设置",modelValue:h(b),"onUpdate:modelValue":u[4]||(u[4]=t=>X(b)?b.value=t:b=t),onOpen:I,class:"set_copyer","show-close":!1,size:550,"before-close":A},{default:a(()=>[e(K,{model:p.value,ref_key:"frontNotifyForm",ref:p,rules:R,"label-width":"120px","label-position":"top"},{default:a(()=>[e(i,{label:"请求地址",prop:"url"},{default:a(()=>[e(m,{type:"warning","show-icon":"",closable:!1},{default:a(()=>[me]),_:1}),e(f,{style:{"margin-top":"10px"},maxlength:100,modelValue:p.value.url,"onUpdate:modelValue":u[0]||(u[0]=t=>p.value.url=t),placeholder:"http://www.cxygzl.com"},null,8,["modelValue"])]),_:1}),e(i,{label:"请求头"},{default:a(()=>[(o(!0),_(v,null,V(p.value.header,(t,n)=>(o(),s(N,{style:{width:"100%","margin-bottom":"20px"},gutter:20},{default:a(()=>[e(d,{span:8},{default:a(()=>[e(i,{prop:"header."+n+".field",rules:[{required:!0,message:"请填写字段名称",trigger:"blur"},{min:1,max:50,message:"1<字段名称长度<50",trigger:"blur"}]},{default:a(()=>[e(f,{modelValue:t.field,"onUpdate:modelValue":l=>t.field=l,maxlength:100,placeholder:"接收字段，比如userName"},null,8,["modelValue","onUpdate:modelValue"])]),_:2},1032,["prop"])]),_:2},1024),e(d,{span:8},{default:a(()=>[e(F,{onChange:l=>q("frontNotify","header",n),modelValue:t.valueMode,"onUpdate:modelValue":l=>t.valueMode=l,size:"large","active-text":"固定值","inactive-text":"表单"},null,8,["onChange","modelValue","onUpdate:modelValue"])]),_:2},1024),e(d,{span:7},{default:a(()=>[e(i,{prop:"header."+n+".value",rules:[{required:!0,message:"请填写字段对应值",trigger:"blur"},{min:1,max:50,message:"1<字段对应值长度<50",trigger:"blur"}]},{default:a(()=>[t.valueMode?(o(),s(f,{key:0,maxlength:100,modelValue:t.value,"onUpdate:modelValue":l=>t.value=l,placeholder:"固定内容"},null,8,["modelValue","onUpdate:modelValue"])):(o(),s(E,{key:1,modelValue:t.value,"onUpdate:modelValue":l=>t.value=l,placeholder:"选择表单",style:{width:"100%"}},{default:a(()=>[(o(!0),_(v,null,V(h(S),l=>(o(),s(x,{key:l.id,label:l.name,value:l.id},null,8,["label","value"]))),128))]),_:2},1032,["modelValue","onUpdate:modelValue"]))]),_:2},1032,["prop"])]),_:2},1024),e(d,{span:1},{default:a(()=>[e(c,{onClick:l=>U("frontNotify","header",n),text:"",icon:r.$icon.Delete},null,8,["onClick","icon"])]),_:2},1024)]),_:2},1024))),256)),e(c,{text:"",type:"primary",onClick:u[1]||(u[1]=t=>C("frontNotify","header")),icon:r.$icon.Plus},{default:a(()=>[$("添加一行 ")]),_:1},8,["icon"])]),_:1}),e(i,{label:"请求体"},{default:a(()=>[(o(!0),_(v,null,V(p.value.body,(t,n)=>(o(),s(N,{style:{width:"100%","margin-bottom":"20px"},gutter:20},{default:a(()=>[e(d,{span:8},{default:a(()=>[e(i,{prop:"body."+n+".field",rules:[{required:!0,message:"请填写字段名称",trigger:"blur"},{min:1,max:50,message:"1<字段名称长度<50",trigger:"blur"}]},{default:a(()=>[e(f,{modelValue:t.field,"onUpdate:modelValue":l=>t.field=l,maxlength:100,placeholder:"接收字段，比如userName"},null,8,["modelValue","onUpdate:modelValue"])]),_:2},1032,["prop"])]),_:2},1024),e(d,{span:8},{default:a(()=>[e(F,{onChange:l=>q("frontNotify","body",n),modelValue:t.valueMode,"onUpdate:modelValue":l=>t.valueMode=l,size:"large","active-text":"固定值","inactive-text":"表单"},null,8,["onChange","modelValue","onUpdate:modelValue"])]),_:2},1024),e(d,{span:7},{default:a(()=>[e(i,{prop:"body."+n+".value",rules:[{required:!0,message:"请填写字段对应值",trigger:"blur"},{min:1,max:50,message:"1<字段对应值长度<50",trigger:"blur"}]},{default:a(()=>[t.valueMode?(o(),s(f,{key:0,maxlength:100,modelValue:t.value,"onUpdate:modelValue":l=>t.value=l,placeholder:"固定内容"},null,8,["modelValue","onUpdate:modelValue"])):(o(),s(E,{key:1,modelValue:t.value,"onUpdate:modelValue":l=>t.value=l,placeholder:"选择表单",style:{width:"100%"}},{default:a(()=>[(o(!0),_(v,null,V(h(S),l=>(o(),s(x,{key:l.id,label:l.name,value:l.id},null,8,["label","value"]))),128))]),_:2},1032,["modelValue","onUpdate:modelValue"]))]),_:2},1032,["prop"])]),_:2},1024),e(d,{span:1},{default:a(()=>[e(c,{onClick:l=>U("frontNotify","body",n),text:"",icon:r.$icon.Delete},null,8,["onClick","icon"])]),_:2},1024)]),_:2},1024))),256)),e(c,{text:"",type:"primary",onClick:u[2]||(u[2]=t=>C("frontNotify","body")),icon:r.$icon.Plus},{default:a(()=>[$("添加一行 ")]),_:1},8,["icon"])]),_:1}),e(i,{label:"返回值"},{default:a(()=>[e(m,{type:"warning","show-icon":"",closable:!1,style:{"margin-bottom":"10px"}},{default:a(()=>[fe]),_:1}),(o(!0),_(v,null,V(p.value.result,(t,n)=>(o(),s(N,{style:{width:"100%","margin-bottom":"20px"},gutter:20},{default:a(()=>[e(d,{span:10},{default:a(()=>[e(i,{prop:"result."+n+".value",rules:[{required:!0,message:"请选择表单",trigger:"blur"}]},{default:a(()=>[e(E,{modelValue:t.value,"onUpdate:modelValue":l=>t.value=l,placeholder:"选择表单",style:{width:"100%"}},{default:a(()=>[(o(!0),_(v,null,V(h(D),l=>(o(),s(x,{key:l.id,label:l.name,value:l.id},null,8,["label","value"]))),128))]),_:2},1032,["modelValue","onUpdate:modelValue"])]),_:2},1032,["prop"])]),_:2},1024),e(d,{span:13},{default:a(()=>[e(i,{prop:"result."+n+".field",rules:[{required:!0,message:"请填写字段名称",trigger:"blur"},{min:1,max:50,message:"1<字段名称长度<50",trigger:"blur"}]},{default:a(()=>[e(f,{modelValue:t.field,"onUpdate:modelValue":l=>t.field=l,maxlength:100,placeholder:"接收字段，比如userName"},null,8,["modelValue","onUpdate:modelValue"])]),_:2},1032,["prop"])]),_:2},1024),e(d,{span:1},{default:a(()=>[e(c,{onClick:l=>U("frontNotify","result",n),text:"",icon:r.$icon.Delete},null,8,["onClick","icon"])]),_:2},1024)]),_:2},1024))),256)),e(c,{text:"",type:"primary",onClick:u[3]||(u[3]=t=>C("frontNotify","result")),icon:r.$icon.Plus},{default:a(()=>[$("添加一行 ")]),_:1},8,["icon"])]),_:1})]),_:1},8,["model","rules"])]),_:1},8,["modelValue"])}}});export{$e as _};