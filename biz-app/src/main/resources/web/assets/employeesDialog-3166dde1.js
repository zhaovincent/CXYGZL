import{r as m,D as u,L as V,h as S,o as w,a as R,l as N,w as c,a5 as f,a3 as _,f as r,k as g,as as h,aB as I}from"./index-e01dade3.js";import{E as U}from"./el-dialog-1471a1af.js";import{d as D,s as $,a as A}from"./selectBox-a8819872.js";import M from"./selectResult-ca10c7f8.js";import{_ as T}from"./_plugin-vue_export-helper-c27b6911.js";import"./refs-dd65b3fd.js";import"./el-avatar-63a3488d.js";import"./el-checkbox-123bf6dd.js";import"./isEqual-47390e81.js";import"./_Uint8Array-aeb50f83.js";import"./flatten-178b99bc.js";import"./_baseFlatten-6e5398bc.js";import"./_overRest-a79e0547.js";/* empty css                */import"./index-8f0c79b2.js";import"./index-126d0406.js";import"./flow-2349aa77.js";const j={class:"person_body clear"},q={class:"person_tree l"},z={__name:"employeesDialog",props:{visible:{type:Boolean,default:!1},data:{type:Array,default:()=>[]},type:{type:String,default:"org"},multiple:{type:Boolean,default:!0},selectSelf:{type:Boolean,default:!0}},emits:["update:visible","change"],setup(n,{emit:v}){const o=n,p=m();let a=m([]),d=u({get(){return o.visible},set(){x()}});const B=(t,l)=>a.value.filter(s=>s.id===t&&s.type===l).length>0;let b=u(()=>{let t=D.value;return[{type:"dept",data:t==null?[]:t.childDepartments},{type:"role",data:t==null?[]:t.roleList},{type:"user",data:t==null?[]:t.employees,change:l=>{B(l.id,l.type)?a.value=a.value.filter(s=>!(s.id===l.id&&s.type===l.type)):(o.multiple||(a.value=[]),a.value.push(l))}}]}),k=u(()=>{let t=a.value.filter(e=>e.type==="user"),l=a.value.filter(e=>e.type==="dept"),s=a.value.filter(e=>e.type==="role"),i=[{type:"user",data:t,cancel:e=>{e.selected=!1,p.value.changeEvent(e)}}];return(o.type==="org"||o.type==="dept")&&i.unshift({type:"dept",data:l,cancel:e=>{e.selected=!1,p.value.changeEvent(e)}}),o.type==="role"&&i.unshift({type:"role",data:s,cancel:e=>{e.selected=!1,p.value.changeEvent(e)}}),i});V(()=>o.visible,t=>{t&&(a.value=o.data,$.value="")});const x=()=>{v("update:visible",!1)};let L=u(()=>D.value?a.value.length:0);const{proxy:y}=S();let C=()=>{var t=a.value;let l=y.$deepCopy(t).map(s=>({type:s.type,id:s.id,name:s.name,avatar:s.avatar}));v("change",l)};const E=()=>{for(var t of y.$deepCopy(a.value))t.selected=!1,p.value.changeEvent(t);a.value=[]};return w(()=>{}),(t,l)=>{const s=I,i=U;return R(),N(i,{title:"选择成员",modelValue:r(d),"onUpdate:modelValue":l[2]||(l[2]=e=>h(d)?d.value=e:d=e),width:600,"append-to-body":"",class:"promoter_person"},{footer:c(()=>[f(s,{onClick:l[1]||(l[1]=e=>t.$emit("update:visible",!1))},{default:c(()=>[_("取 消")]),_:1}),f(s,{type:"primary",onClick:r(C)},{default:c(()=>[_("确 定")]),_:1},8,["onClick"])]),default:c(()=>[g("div",j,[g("div",q,[f(A,{ref_key:"selectBoxRef",ref:p,selectSelf:n.selectSelf,list:r(b),multiple:n.multiple,selectedList:r(a),"onUpdate:selectedList":l[0]||(l[0]=e=>h(a)?a.value=e:a=e),type:n.type},null,8,["selectSelf","list","multiple","selectedList","type"])]),f(M,{total:r(L),onDel:E,list:r(k)},null,8,["total","list"])])]),_:1},8,["modelValue"])}}},oe=T(z,[["__scopeId","data-v-5e8fce32"]]);export{oe as default};