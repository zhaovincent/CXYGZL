import{r as y,D as u,L as V,h as S,o as w,a as R,l as N,w as c,a5 as f,a3 as g,f as r,k as h,as as _,aD as I}from"./index-d5547550.js";/* empty css                  */import U from"./selectBox-26a3a753.js";import $ from"./selectResult-4abbf083.js";import{d as D,s as A}from"./selectBox.vue_vue_type_style_index_0_scoped_82c14c6c_lang-f1c14f73.js";/* empty css                                                                        */import{_ as M}from"./_plugin-vue_export-helper-c27b6911.js";import{E as T}from"./index-a6355ec2.js";/* empty css                  *//* empty css                    *//* empty css                */import"./index-222e6800.js";import"./isEqual-fe47b28a.js";import"./_Uint8Array-c7447712.js";import"./flatten-36990bb8.js";import"./_baseFlatten-c80c9ba3.js";import"./_overRest-d3d6b67b.js";import"./index-85bc974f.js";/* empty css                                                                     */import"./index-a7c7b111.js";import"./flow-165b4256.js";import"./const-a11e1fef.js";import"./use-dialog-27a1e524.js";import"./refs-5fd04da2.js";const j={class:"person_body clear"},q={class:"person_tree l"},z={__name:"employeesDialog",props:{visible:{type:Boolean,default:!1},data:{type:Array,default:()=>[]},type:{type:String,default:"org"},multiple:{type:Boolean,default:!0},selectSelf:{type:Boolean,default:!0}},emits:["update:visible","change"],setup(n,{emit:m}){const s=n,i=y();let a=y([]),d=u({get(){return s.visible},set(){L()}});const b=(t,l)=>a.value.filter(o=>o.id===t&&o.type===l).length>0;let B=u(()=>{let t=D.value;return[{type:"dept",data:t==null?[]:t.childDepartments},{type:"role",data:t==null?[]:t.roleList},{type:"user",data:t==null?[]:t.employees,change:l=>{b(l.id,l.type)?a.value=a.value.filter(o=>!(o.id===l.id&&o.type===l.type)):(s.multiple||(a.value=[]),a.value.push(l))}}]}),k=u(()=>{let t=a.value.filter(e=>e.type==="user"),l=a.value.filter(e=>e.type==="dept"),o=a.value.filter(e=>e.type==="role"),p=[{type:"user",data:t,cancel:e=>{e.selected=!1,i.value.changeEvent(e)}}];return(s.type==="org"||s.type==="dept")&&p.unshift({type:"dept",data:l,cancel:e=>{e.selected=!1,i.value.changeEvent(e)}}),s.type==="role"&&p.unshift({type:"role",data:o,cancel:e=>{e.selected=!1,i.value.changeEvent(e)}}),p});V(()=>s.visible,t=>{t&&(a.value=s.data,A.value="")});const L=()=>{m("update:visible",!1)};let x=u(()=>D.value?a.value.length:0);const{proxy:v}=S();let C=()=>{var t=a.value;let l=v.$deepCopy(t).map(o=>({type:o.type,id:o.id,name:o.name,avatar:o.avatar}));m("change",l)};const E=()=>{for(var t of v.$deepCopy(a.value))t.selected=!1,i.value.changeEvent(t);a.value=[]};return w(()=>{}),(t,l)=>{const o=I,p=T;return R(),N(p,{title:"选择成员",modelValue:r(d),"onUpdate:modelValue":l[2]||(l[2]=e=>_(d)?d.value=e:d=e),width:600,"append-to-body":"",class:"promoter_person"},{footer:c(()=>[f(o,{onClick:l[1]||(l[1]=e=>t.$emit("update:visible",!1))},{default:c(()=>[g("取 消")]),_:1}),f(o,{type:"primary",onClick:r(C)},{default:c(()=>[g("确 定")]),_:1},8,["onClick"])]),default:c(()=>[h("div",j,[h("div",q,[f(U,{ref_key:"selectBoxRef",ref:i,selectSelf:n.selectSelf,list:r(B),multiple:n.multiple,selectedList:r(a),"onUpdate:selectedList":l[0]||(l[0]=e=>_(a)?a.value=e:a=e),type:n.type},null,8,["selectSelf","list","multiple","selectedList","type"])]),f($,{total:r(x),onDel:E,list:r(k)},null,8,["total","list"])])]),_:1},8,["modelValue"])}}},ce=M(z,[["__scopeId","data-v-5ecf5e86"]]);export{ce as default};