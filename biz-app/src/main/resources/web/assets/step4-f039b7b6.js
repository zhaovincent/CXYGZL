import{d as W,h as Z,K as j,ab as G,o as H,D as q,a as t,c as f,k as w,a5 as e,w as a,a3 as h,f as d,aZ as R,l as i,R as b,a6 as y,am as T,E as J,aC as Q,aD as X,ag as Y,ah as ee}from"./index-d5547550.js";import{E as le}from"./el-card-39a9c0d0.js";/* empty css                */import{a as ae,E as oe}from"./el-row-a594fbeb.js";import"./el-tag-de6752b8.js";import{E as te,a as ue}from"./el-select-46753b34.js";import"./el-scrollbar-cbe0c54f.js";import"./el-popper-00977718.js";import{a as re,E as ne}from"./el-form-item-2de9c3c8.js";import{E as de}from"./el-alert-867b40be.js";import{E as se}from"./el-switch-a81630c6.js";import{E as pe}from"./el-text-a7cd3acd.js";/* empty css                */import{u as ie}from"./flow-165b4256.js";import{_ as me}from"./_plugin-vue_export-helper-c27b6911.js";import"./strings-fbdcd853.js";import"./isEqual-fe47b28a.js";import"./_Uint8Array-c7447712.js";import"./debounce-e2bc871f.js";import"./index-e8e7f773.js";import"./_baseClone-e7eb389f.js";const I=M=>(Y("data-v-b7f1338d"),M=M(),ee(),M),fe={class:"container-div",style:{"margin-top":"20px"}},ce=I(()=>w("p",null,"仅支持POST请求，以请求体方式接收参数",-1)),ge=I(()=>w("p",null,"通过接口可以修改表单值：左侧表单表示要修改的表单值，右侧字段是指接口返回的字段名",-1)),_e=I(()=>w("p",null,"仅支持POST请求，以请求体方式接收参数",-1)),be=I(()=>w("p",null,"通过接口可以修改表单值：左侧表单表示要修改的表单值，右侧字段是指接口返回的字段名",-1)),ye=W({__name:"step4",setup(M,{expose:A}){const{proxy:F}=Z(),C=(r,u)=>{S.addStep4OneLine(r,u)},N=(r,u,x)=>{S.delStep4OneLine(r,u,x)},z=(r,u,x)=>{S.clearStep4Value(r,u,x)};A({validate:r=>{let u=V.value.enable;v.value.enable?F.$refs.frontNotifyForm.validate((k,c)=>{var g=[];if(!k)for(var m in c)g.push(c[m][0].message);u?F.$refs.backNotifyForm.validate((p,s)=>{if(!p)for(var U in s)g.push(s[U][0].message);r(p&&k,g)}):r(k,g)}):u?F.$refs.backNotifyForm.validate((k,c)=>{var g=[];if(!k)for(var m in c)g.push(c[m][0].message);r(k,g)}):r(!0)}});const L=j({url:[{required:!0,message:"请填写请求地址",type:"url",trigger:"blur"}]});G(),H(()=>{});let S=ie();var O=q(()=>S.step4),P=q(()=>S.step2.filter(r=>r.type!="Description")),D=q(()=>{let r=F.$deepCopy(P.value);return r.push({id:"root",type:"SelectUser",name:"发起人"}),r}),v=q(()=>O.value.frontNotify),V=q(()=>O.value.backNotify);return(r,u)=>{const x=J,k=pe,c=se,g=de,m=Q,p=re,s=ae,U=te,$=ue,_=X,E=oe,B=ne,K=le;return t(),f("div",null,[w("div",fe,[e(K,{class:"box-card"},{default:a(()=>[w("h3",null,[h("前置通知 "),e(k,{type:"info",size:"small"},{default:a(()=>[e(x,null,{default:a(()=>[e(d(R))]),_:1}),h(" 流程启动时触发 ")]),_:1}),e(c,{modelValue:d(v).enable,"onUpdate:modelValue":u[0]||(u[0]=o=>d(v).enable=o),size:"large"},null,8,["modelValue"])]),d(v).enable?(t(),i(B,{key:0,model:d(v),ref_key:"frontNotifyForm",ref:v,rules:L,"label-width":"120px","label-position":"top"},{default:a(()=>[e(p,{label:"请求地址",prop:"url"},{default:a(()=>[e(g,{type:"warning","show-icon":"",closable:!1},{default:a(()=>[ce]),_:1}),e(m,{style:{"margin-top":"10px"},maxlength:100,modelValue:d(v).url,"onUpdate:modelValue":u[1]||(u[1]=o=>d(v).url=o),placeholder:"http://www.cxygzl.com"},null,8,["modelValue"])]),_:1}),e(p,{label:"请求头"},{default:a(()=>[(t(!0),f(b,null,y(d(v).header,(o,n)=>(t(),i(E,{style:{width:"100%","margin-bottom":"20px"},gutter:20},{default:a(()=>[e(s,{span:9},{default:a(()=>[e(p,{prop:"header."+n+".field",rules:[{required:!0,message:"请填写字段名称",trigger:"blur"},{min:1,max:50,message:"1<字段名称长度<50",trigger:"blur"}]},{default:a(()=>[e(m,{modelValue:o.field,"onUpdate:modelValue":l=>o.field=l,maxlength:100,placeholder:"接收字段，比如userName"},null,8,["modelValue","onUpdate:modelValue"])]),_:2},1032,["prop"])]),_:2},1024),e(s,{span:6},{default:a(()=>[e(c,{onChange:l=>z("frontNotify","header",n),modelValue:o.valueMode,"onUpdate:modelValue":l=>o.valueMode=l,size:"large","active-text":"固定值","inactive-text":"表单"},null,8,["onChange","modelValue","onUpdate:modelValue"])]),_:2},1024),e(s,{span:8},{default:a(()=>[e(p,{prop:"header."+n+".value",rules:[{required:!0,message:"请填写字段对应值",trigger:"blur"},{min:1,max:50,message:"1<字段对应值长度<50",trigger:"blur"}]},{default:a(()=>[o.valueMode?(t(),i(m,{key:0,maxlength:100,modelValue:o.value,"onUpdate:modelValue":l=>o.value=l,placeholder:"固定内容"},null,8,["modelValue","onUpdate:modelValue"])):(t(),i($,{key:1,modelValue:o.value,"onUpdate:modelValue":l=>o.value=l,placeholder:"选择表单",style:{width:"100%"}},{default:a(()=>[(t(!0),f(b,null,y(d(D),l=>(t(),i(U,{key:l.id,label:l.name,value:l.id},null,8,["label","value"]))),128))]),_:2},1032,["modelValue","onUpdate:modelValue"]))]),_:2},1032,["prop"])]),_:2},1024),e(s,{span:1},{default:a(()=>[e(_,{onClick:l=>N("frontNotify","header",n),text:"",icon:r.$icon.Delete},null,8,["onClick","icon"])]),_:2},1024)]),_:2},1024))),256)),e(_,{text:"",type:"primary",onClick:u[2]||(u[2]=o=>C("frontNotify","header")),icon:r.$icon.Plus},{default:a(()=>[h("添加一行 ")]),_:1},8,["icon"])]),_:1}),e(p,{label:"请求体"},{default:a(()=>[(t(!0),f(b,null,y(d(v).body,(o,n)=>(t(),i(E,{style:{width:"100%","margin-bottom":"20px"},gutter:20},{default:a(()=>[e(s,{span:9},{default:a(()=>[e(p,{prop:"body."+n+".field",rules:[{required:!0,message:"请填写字段名称",trigger:"blur"},{min:1,max:50,message:"1<字段名称长度<50",trigger:"blur"}]},{default:a(()=>[e(m,{modelValue:o.field,"onUpdate:modelValue":l=>o.field=l,maxlength:100,placeholder:"接收字段，比如userName"},null,8,["modelValue","onUpdate:modelValue"])]),_:2},1032,["prop"])]),_:2},1024),e(s,{span:6},{default:a(()=>[e(c,{onChange:l=>z("frontNotify","body",n),modelValue:o.valueMode,"onUpdate:modelValue":l=>o.valueMode=l,size:"large","active-text":"固定值","inactive-text":"表单"},null,8,["onChange","modelValue","onUpdate:modelValue"])]),_:2},1024),e(s,{span:8},{default:a(()=>[e(p,{prop:"body."+n+".value",rules:[{required:!0,message:"请填写字段对应值",trigger:"blur"},{min:1,max:50,message:"1<字段对应值长度<50",trigger:"blur"}]},{default:a(()=>[o.valueMode?(t(),i(m,{key:0,maxlength:100,modelValue:o.value,"onUpdate:modelValue":l=>o.value=l,placeholder:"固定内容"},null,8,["modelValue","onUpdate:modelValue"])):(t(),i($,{key:1,modelValue:o.value,"onUpdate:modelValue":l=>o.value=l,placeholder:"选择表单",style:{width:"100%"}},{default:a(()=>[(t(!0),f(b,null,y(d(D),l=>(t(),i(U,{key:l.id,label:l.name,value:l.id},null,8,["label","value"]))),128))]),_:2},1032,["modelValue","onUpdate:modelValue"]))]),_:2},1032,["prop"])]),_:2},1024),e(s,{span:1},{default:a(()=>[e(_,{onClick:l=>N("frontNotify","body",n),text:"",icon:r.$icon.Delete},null,8,["onClick","icon"])]),_:2},1024)]),_:2},1024))),256)),e(_,{text:"",type:"primary",onClick:u[3]||(u[3]=o=>C("frontNotify","body")),icon:r.$icon.Plus},{default:a(()=>[h("添加一行 ")]),_:1},8,["icon"])]),_:1}),e(p,{label:"返回值"},{default:a(()=>[e(g,{type:"warning","show-icon":"",closable:!1,style:{"margin-bottom":"10px"}},{default:a(()=>[ge]),_:1}),(t(!0),f(b,null,y(d(v).result,(o,n)=>(t(),i(E,{style:{width:"100%","margin-bottom":"20px"},gutter:20},{default:a(()=>[e(s,{span:10},{default:a(()=>[e(p,{prop:"result."+n+".value",rules:[{required:!0,message:"请选择表单",trigger:"blur"}]},{default:a(()=>[e($,{modelValue:o.value,"onUpdate:modelValue":l=>o.value=l,placeholder:"选择表单",style:{width:"100%"}},{default:a(()=>[(t(!0),f(b,null,y(d(P),l=>(t(),i(U,{key:l.id,label:l.name,value:l.id},null,8,["label","value"]))),128))]),_:2},1032,["modelValue","onUpdate:modelValue"])]),_:2},1032,["prop"])]),_:2},1024),e(s,{span:13},{default:a(()=>[e(p,{prop:"result."+n+".field",rules:[{required:!0,message:"请填写字段名称",trigger:"blur"},{min:1,max:50,message:"1<字段名称长度<50",trigger:"blur"}]},{default:a(()=>[e(m,{modelValue:o.field,"onUpdate:modelValue":l=>o.field=l,maxlength:100,placeholder:"接收字段，比如userName"},null,8,["modelValue","onUpdate:modelValue"])]),_:2},1032,["prop"])]),_:2},1024),e(s,{span:1},{default:a(()=>[e(_,{onClick:l=>N("frontNotify","result",n),text:"",icon:r.$icon.Delete},null,8,["onClick","icon"])]),_:2},1024)]),_:2},1024))),256)),e(_,{text:"",type:"primary",onClick:u[4]||(u[4]=o=>C("frontNotify","result")),icon:r.$icon.Plus},{default:a(()=>[h("添加一行 ")]),_:1},8,["icon"])]),_:1})]),_:1},8,["model","rules"])):T("",!0),w("h3",null,[h("后置通知 "),e(k,{type:"info",size:"small"},{default:a(()=>[e(x,null,{default:a(()=>[e(d(R))]),_:1}),h(" 流程结束时触发 ")]),_:1}),e(c,{modelValue:d(V).enable,"onUpdate:modelValue":u[5]||(u[5]=o=>d(V).enable=o),size:"large"},null,8,["modelValue"])]),d(V).enable?(t(),i(B,{key:1,model:d(V),ref_key:"backNotifyForm",ref:V,rules:L,"label-width":"120px","label-position":"top"},{default:a(()=>[e(p,{label:"请求地址",prop:"url"},{default:a(()=>[e(g,{type:"warning","show-icon":"",closable:!1},{default:a(()=>[_e]),_:1}),e(m,{style:{"margin-top":"10px"},maxlength:100,modelValue:d(V).url,"onUpdate:modelValue":u[6]||(u[6]=o=>d(V).url=o),placeholder:"http://www.cxygzl.com"},null,8,["modelValue"])]),_:1}),e(p,{label:"请求头"},{default:a(()=>[(t(!0),f(b,null,y(d(V).header,(o,n)=>(t(),i(E,{style:{width:"100%","margin-bottom":"20px"},gutter:20},{default:a(()=>[e(s,{span:9},{default:a(()=>[e(p,{prop:"header."+n+".field",rules:[{required:!0,message:"请填写字段名称",trigger:"blur"},{min:1,max:50,message:"1<字段名称长度<50",trigger:"blur"}]},{default:a(()=>[e(m,{modelValue:o.field,"onUpdate:modelValue":l=>o.field=l,maxlength:100,placeholder:"接收字段，比如userName"},null,8,["modelValue","onUpdate:modelValue"])]),_:2},1032,["prop"])]),_:2},1024),e(s,{span:6},{default:a(()=>[e(c,{onChange:l=>z("backNotify","header",n),modelValue:o.valueMode,"onUpdate:modelValue":l=>o.valueMode=l,size:"large","active-text":"固定值","inactive-text":"表单"},null,8,["onChange","modelValue","onUpdate:modelValue"])]),_:2},1024),e(s,{span:8},{default:a(()=>[e(p,{prop:"header."+n+".value",rules:[{required:!0,message:"请填写字段对应值",trigger:"blur"},{min:1,max:50,message:"1<字段对应值长度<50",trigger:"blur"}]},{default:a(()=>[o.valueMode?(t(),i(m,{key:0,maxlength:100,modelValue:o.value,"onUpdate:modelValue":l=>o.value=l,placeholder:"固定内容"},null,8,["modelValue","onUpdate:modelValue"])):(t(),i($,{key:1,modelValue:o.value,"onUpdate:modelValue":l=>o.value=l,placeholder:"选择表单",style:{width:"100%"}},{default:a(()=>[(t(!0),f(b,null,y(d(D),l=>(t(),i(U,{key:l.id,label:l.name,value:l.id},null,8,["label","value"]))),128))]),_:2},1032,["modelValue","onUpdate:modelValue"]))]),_:2},1032,["prop"])]),_:2},1024),e(s,{span:1},{default:a(()=>[e(_,{onClick:l=>N("backNotify","header",n),text:"",icon:r.$icon.Delete},null,8,["onClick","icon"])]),_:2},1024)]),_:2},1024))),256)),e(_,{text:"",type:"primary",onClick:u[7]||(u[7]=o=>C("backNotify","header")),icon:r.$icon.Plus},{default:a(()=>[h("添加一行 ")]),_:1},8,["icon"])]),_:1}),e(p,{label:"请求体"},{default:a(()=>[(t(!0),f(b,null,y(d(V).body,(o,n)=>(t(),i(E,{style:{width:"100%","margin-bottom":"20px"},gutter:20},{default:a(()=>[e(s,{span:9},{default:a(()=>[e(p,{prop:"body."+n+".field",rules:[{required:!0,message:"请填写字段名称",trigger:"blur"},{min:1,max:50,message:"1<字段名称长度<50",trigger:"blur"}]},{default:a(()=>[e(m,{modelValue:o.field,"onUpdate:modelValue":l=>o.field=l,maxlength:100,placeholder:"接收字段，比如userName"},null,8,["modelValue","onUpdate:modelValue"])]),_:2},1032,["prop"])]),_:2},1024),e(s,{span:6},{default:a(()=>[e(c,{onChange:l=>z("backNotify","body",n),modelValue:o.valueMode,"onUpdate:modelValue":l=>o.valueMode=l,size:"large","active-text":"固定值","inactive-text":"表单"},null,8,["onChange","modelValue","onUpdate:modelValue"])]),_:2},1024),e(s,{span:8},{default:a(()=>[e(p,{prop:"body."+n+".value",rules:[{required:!0,message:"请填写字段对应值",trigger:"blur"},{min:1,max:50,message:"1<字段对应值长度<50",trigger:"blur"}]},{default:a(()=>[o.valueMode?(t(),i(m,{key:0,maxlength:100,modelValue:o.value,"onUpdate:modelValue":l=>o.value=l,placeholder:"固定内容"},null,8,["modelValue","onUpdate:modelValue"])):(t(),i($,{key:1,modelValue:o.value,"onUpdate:modelValue":l=>o.value=l,placeholder:"选择表单",style:{width:"100%"}},{default:a(()=>[(t(!0),f(b,null,y(d(D),l=>(t(),i(U,{key:l.id,label:l.name,value:l.id},null,8,["label","value"]))),128))]),_:2},1032,["modelValue","onUpdate:modelValue"]))]),_:2},1032,["prop"])]),_:2},1024),e(s,{span:1},{default:a(()=>[e(_,{onClick:l=>N("backNotify","body",n),text:"",icon:r.$icon.Delete},null,8,["onClick","icon"])]),_:2},1024)]),_:2},1024))),256)),e(_,{text:"",type:"primary",onClick:u[8]||(u[8]=o=>C("backNotify","body")),icon:r.$icon.Plus},{default:a(()=>[h("添加一行 ")]),_:1},8,["icon"])]),_:1}),e(p,{label:"返回值"},{default:a(()=>[e(g,{type:"warning","show-icon":"",closable:!1,style:{"margin-bottom":"10px"}},{default:a(()=>[be]),_:1}),(t(!0),f(b,null,y(d(V).result,(o,n)=>(t(),i(E,{style:{width:"100%","margin-bottom":"20px"},gutter:20},{default:a(()=>[e(s,{span:10},{default:a(()=>[e(p,{prop:"result."+n+".value",rules:[{required:!0,message:"请选择表单",trigger:"blur"}]},{default:a(()=>[e($,{modelValue:o.value,"onUpdate:modelValue":l=>o.value=l,placeholder:"选择表单",style:{width:"100%"}},{default:a(()=>[(t(!0),f(b,null,y(d(P),l=>(t(),i(U,{key:l.id,label:l.name,value:l.id},null,8,["label","value"]))),128))]),_:2},1032,["modelValue","onUpdate:modelValue"])]),_:2},1032,["prop"])]),_:2},1024),e(s,{span:13},{default:a(()=>[e(p,{prop:"result."+n+".field",rules:[{required:!0,message:"请填写字段名称",trigger:"blur"},{min:1,max:50,message:"1<字段名称长度<50",trigger:"blur"}]},{default:a(()=>[e(m,{modelValue:o.field,"onUpdate:modelValue":l=>o.field=l,maxlength:100,placeholder:"接收字段，比如userName"},null,8,["modelValue","onUpdate:modelValue"])]),_:2},1032,["prop"])]),_:2},1024),e(s,{span:1},{default:a(()=>[e(_,{onClick:l=>N("backNotify","result",n),text:"",icon:r.$icon.Delete},null,8,["onClick","icon"])]),_:2},1024)]),_:2},1024))),256)),e(_,{text:"",type:"primary",onClick:u[9]||(u[9]=o=>C("backNotify","result")),icon:r.$icon.Plus},{default:a(()=>[h("添加一行 ")]),_:1},8,["icon"])]),_:1})]),_:1},8,["model","rules"])):T("",!0)]),_:1})])])}}});const Re=me(ye,[["__scopeId","data-v-b7f1338d"]]);export{Re as default};
