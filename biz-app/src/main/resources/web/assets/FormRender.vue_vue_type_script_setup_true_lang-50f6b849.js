import{d as h,h as x,o as b,r as k,K as C,D as E,a as n,l,w as i,c as v,R as y,a6 as q,m as w,am as B,f as D}from"./index-e01dade3.js";/* empty css                */import{a as V,E as j}from"./el-form-item-85be6a3a.js";import{g as N}from"./getFormWidget-4084fd80.js";const U=h({__name:"FormRender",props:{formList:{type:Object,default:()=>{}}},emits:["addLayoutOneItem","deleteLayoutOneItem"],setup(u,{expose:L,emit:f}){const d=u,_=o=>N[o],{proxy:m}=x();b(()=>{var t;let o=d.formList;for(var r of o){let a=r.id;if(((t=m.$refs["form"+a])==null?void 0:t.length)>0){let e=m.$refs["form"+a][0].getValidateRule();c[a]=e}}});const I=o=>{f("addLayoutOneItem",o)},O=(o,r)=>{f("deleteLayoutOneItem",o,r)},p=k(),c=C({});L({validate:o=>{p.value.validate(r=>{o(r)})}});const g=E(()=>{var o={};for(var r of d.formList)if(o[r.id]=r.props.value,r.type==="Layout"){let F=r.props.value;var t=[];for(var a of F){var e={};for(var s of a){let R=s.props.value;e[s.id]=R}t.push(e)}o[r.id]=t}return o});return(o,r)=>{const t=V,a=j;return n(),l(a,{"label-position":"top",rules:c,model:D(g),ref_key:"ruleFormRef",ref:p},{default:i(()=>[(n(!0),v(y,null,q(u.formList,(e,s)=>(n(),v(y,null,[e.perm!="H"?(n(),l(t,{key:0,label:e.name+(e.props.unit?"("+e.props.unit+")":""),prop:e?e.id:"",required:e?e.required:!1},{default:i(()=>[(n(),l(w(_(e.type)),{style:{width:"100%"},onAddLayoutOneItem:I,onDeleteLayoutOneItem:O,mode:"RUN",ref_for:!0,ref:"form"+e.id,form:e},null,40,["form"]))]),_:2},1032,["label","prop","required"])):B("",!0)],64))),256))]),_:1},8,["rules","model"])}}});export{U as _};
