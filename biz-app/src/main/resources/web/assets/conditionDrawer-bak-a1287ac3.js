import{r as $,D as h,L as Q,h as W,a,l as f,w as n,k as u,n as X,a5 as r,f as c,a3 as m,c as C,R as x,a6 as V,q as b,am as T,as as Y,aD as Z}from"./index-d5547550.js";import{E as ee}from"./el-drawer-75c80c60.js";import{E as oe}from"./el-card-39a9c0d0.js";import{E as te}from"./el-tag-de6752b8.js";/* empty css                */import{a as ie,E as ne}from"./el-form-item-2de9c3c8.js";import{E as le}from"./el-switch-a81630c6.js";import{$ as U}from"./index-a7c7b111.js";import{u as re}from"./index-71f0fd6b.js";import ae from"./condition-7334fe6b.js";import{u as se}from"./flow-165b4256.js";import{_ as de}from"./_plugin-vue_export-helper-c27b6911.js";import"./use-dialog-27a1e524.js";import"./_baseClone-e7eb389f.js";import"./_Uint8Array-c7447712.js";import"./const-a11e1fef.js";import"./area-23a735cb.js";import"./el-scrollbar-cbe0c54f.js";import"./index-222e6800.js";import"./isEqual-fe47b28a.js";import"./flatten-36990bb8.js";import"./_baseFlatten-c80c9ba3.js";import"./_overRest-d3d6b67b.js";import"./el-radio-92787871.js";import"./strings-fbdcd853.js";import"./arrays-e667dc24.js";import"./cloneDeep-cfc40afc.js";import"./el-popper-00977718.js";import"./index-e8e7f773.js";import"./debounce-e2bc871f.js";/* empty css                    */import"./el-time-picker-f79f3178.js";import"./panel-time-pick-ae955f65.js";import"./index-da53d962.js";import"./isArrayLikeObject-724a27ce.js";import"./el-date-picker-2b967550.js";import"./el-input-number-f4eeab94.js";import"./el-select-46753b34.js";import"./index-6e9421a4.js";import"./selectAndShow.vue_vue_type_script_setup_true_lang-9eafc7ba.js";import"./employeesDialog-54936475.js";/* empty css                  */import"./selectBox-26a3a753.js";/* empty css                  *//* empty css                */import"./selectBox.vue_vue_type_style_index_0_scoped_82c14c6c_lang-f1c14f73.js";import"./index-85bc974f.js";import"./selectResult-4abbf083.js";/* empty css                                                                     *//* empty css                                                                        */import"./index-a6355ec2.js";import"./refs-5fd04da2.js";import"./orgItem-a67c9471.js";const pe=["id"],ce={class:"demo-tagInput"},ue={class:"tag-wrap"},me={class:"tag-wrap"},fe={class:"card-header"},ve={style:{display:"flex","flex-direction":"row","justify-content":"space-between"}},_e={__name:"conditionDrawer-bak",setup(ye){const g=(t,e,i)=>{l.value.groupRelation.push({type:e,exp:i,name:t})},F=t=>{l.value.groupRelation.splice(t,1)},I=()=>{l.value.groupRelation.splice(0,l.value.groupRelation.length)};let d=$({conditionNodes:[]}),l=$({groupRelation:[]}),R=$(""),N=re(),{setCondition:z,setConditionsConfig:M}=N,B=h(()=>N.conditionsConfig1),O=h(()=>N.conditionDrawer),L=h({get(){return O.value},set(){S()}});const G=t=>{var e;(e=l.value)==null||e.conditionList.splice(t,1)},j=(t,e)=>{var i;(i=l.value)==null||i.conditionList[t].conditionList.splice(e,1)},q=()=>{var t;(t=l.value)==null||t.conditionList.push({mode:!0,conditionList:[{}]})},H=(t,e)=>{let i=t.conditionList;i||(i=[]),i.push({}),t.conditionList=i};let P=se();const A=h(()=>P.step2),J=()=>{};Q(B,t=>{let e=t.value.conditionNodes;for(var i of e){let s=i.conditionList;for(var v of s){let E=v.conditionList;for(var _ of E){let k=_.key;if(k==="root")_.keyType="SelectUser";else{let w=A.value.filter(o=>o.id===k);w.length>0&&(_.keyType=w[0].type)}}}}d.value=t.value,R.value=t.priorityLevel,l.value=t.priorityLevel?d.value.conditionNodes[t.priorityLevel-1]:{nodeUserList:[],conditionList:[]}}),W();const K=()=>{S();var t=d.value.conditionNodes.splice(R.value-1,1);d.value.conditionNodes.splice(l.value.priorityLevel-1,0,t[0]),d.value.conditionNodes.map((i,v)=>{i.priorityLevel=v+1});for(var e=0;e<d.value.conditionNodes.length;e++){let i=d.value.conditionNodes[e];i.error=!1,e!=d.value.conditionNodes.length-1&&(i.error=!U.checkCondition(d.value,e)),i.placeHolder=U.conditionStr(d.value,e)}M({value:d.value,flag:!0,id:B.value.id})},S=t=>{z(!1)};return(t,e)=>{const i=le,v=ie,_=ne,s=Z,E=te,k=oe,w=ee;return a(),f(w,{"append-to-body":!0,title:"条件设置",modelValue:c(L),"onUpdate:modelValue":e[6]||(e[6]=o=>Y(L)?L.value=o:L=o),"show-close":!1,size:550,onOpen:J,"before-close":K},{header:n(({titleId:o,titleClass:p})=>[u("h3",{id:o,class:X(p)},"条件设置",10,pe)]),default:n(()=>[r(_,{"label-width":"120px"},{default:n(()=>[r(v,{label:"条件组关系"},{default:n(()=>[r(i,{modelValue:c(l).groupRelationMode,"onUpdate:modelValue":e[0]||(e[0]=o=>c(l).groupRelationMode=o),"active-text":"固定关系","inactive-text":"自定义关系"},null,8,["modelValue"])]),_:1})]),_:1}),c(l).groupRelationMode?(a(),f(_,{key:0,"label-width":"120px"},{default:n(()=>[r(v,{label:"固定关系"},{default:n(()=>[r(i,{modelValue:c(l).mode,"onUpdate:modelValue":e[1]||(e[1]=o=>c(l).mode=o),"active-text":"且","inactive-text":"或"},null,8,["modelValue"])]),_:1})]),_:1})):(a(),f(_,{key:1,"label-width":"120px"},{default:n(()=>[r(v,{label:"自定义关系"},{default:n(()=>[r(s,{icon:t.$icon.Delete,onClick:I,type:"danger",text:""},{default:n(()=>[m("清空选项")]),_:1},8,["icon"]),r(k,{style:{"min-height":"200px",width:"100%"}},{default:n(()=>[(a(!0),C(x,null,V(c(l).groupRelation,(o,p)=>(a(),f(E,{style:{"margin-bottom":"10px"},key:o.name,class:"mx-1",size:"large",closable:"",onClose:y=>F(p),type:o.type},{default:n(()=>[m(b(o.name),1)]),_:2},1032,["onClose","type"]))),128))]),_:1}),u("div",ce,[u("div",ue,[r(s,{type:"success",onClick:e[2]||(e[2]=o=>g("或","success","||"))},{default:n(()=>[m(" 或 ")]),_:1}),r(s,{type:"success",onClick:e[3]||(e[3]=o=>g("且","success","&&"))},{default:n(()=>[m(" 且 ")]),_:1}),r(s,{type:"danger",onClick:e[4]||(e[4]=o=>g("(","danger","("))},{default:n(()=>[m(" ( ")]),_:1}),r(s,{type:"danger",onClick:e[5]||(e[5]=o=>g(")","danger",")"))},{default:n(()=>[m(" ) ")]),_:1})]),u("div",me,[(a(!0),C(x,null,V(c(l).conditionList.length,o=>(a(),f(s,{type:"primary",style:{"margin-top":"10px"},key:o,onClick:p=>g("条件组"+o,"primary","c"+o)},{default:n(()=>[m(b("条件组"+o),1)]),_:2},1032,["onClick"]))),128))])])]),_:1})]),_:1})),(a(!0),C(x,null,V(c(l).conditionList,(o,p)=>(a(),f(k,{class:"box-card",key:p,style:{"margin-bottom":"20px"}},{header:n(()=>[u("div",fe,[u("span",null,"条件组"+b(p+1),1),r(i,{modelValue:o.mode,"onUpdate:modelValue":y=>o.mode=y,"active-text":"且","inactive-text":"或"},null,8,["modelValue","onUpdate:modelValue"]),c(l).conditionList.length>1?(a(),f(s,{key:0,text:"",onClick:y=>G(p),icon:t.$icon.Delete},null,8,["onClick","icon"])):T("",!0)])]),default:n(()=>[(a(!0),C(x,null,V(o.conditionList,(y,D)=>(a(),C("div",{key:D},[u("div",ve,[u("div",null,b(D==0?"当":o.mode?"且":"或"),1),u("div",null,[o.conditionList.length>1?(a(),f(s,{key:0,text:"",onClick:ge=>j(p,D),icon:t.$icon.Delete},null,8,["onClick","icon"])):T("",!0)])]),r(ae,{condition:y},null,8,["condition"])]))),128)),r(s,{dark:"",type:"success",style:{"margin-top":"20px"},onClick:y=>H(o,p)},{default:n(()=>[m("添加条件")]),_:2},1032,["onClick"])]),_:2},1024))),128)),r(s,{dark:"",type:"primary",onClick:q},{default:n(()=>[m("添加条件组")]),_:1})]),_:1},8,["modelValue"])}}},ko=de(_e,[["__scopeId","data-v-089d959e"]]);export{ko as default};