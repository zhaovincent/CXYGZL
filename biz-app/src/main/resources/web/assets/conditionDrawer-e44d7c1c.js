import{r as m,D as s,L as b,h as B,a as E,l as z,w as _,k as F,n as I,a5 as R,f as y,as as g}from"./index-d5547550.js";import{E as T}from"./el-drawer-75c80c60.js";import{$ as w}from"./index-a7c7b111.js";import{u as G}from"./index-71f0fd6b.js";import H from"./conditionGroup-01fc4cb1.js";import{u as O}from"./flow-165b4256.js";import{_ as P}from"./_plugin-vue_export-helper-c27b6911.js";import"./use-dialog-27a1e524.js";import"./const-a11e1fef.js";import"./el-card-39a9c0d0.js";import"./el-tag-de6752b8.js";/* empty css                */import"./el-form-item-2de9c3c8.js";import"./_baseClone-e7eb389f.js";import"./_Uint8Array-c7447712.js";import"./el-switch-a81630c6.js";import"./condition-7334fe6b.js";import"./area-23a735cb.js";import"./el-scrollbar-cbe0c54f.js";import"./index-222e6800.js";import"./isEqual-fe47b28a.js";import"./flatten-36990bb8.js";import"./_baseFlatten-c80c9ba3.js";import"./_overRest-d3d6b67b.js";import"./el-radio-92787871.js";import"./strings-fbdcd853.js";import"./arrays-e667dc24.js";import"./cloneDeep-cfc40afc.js";import"./el-popper-00977718.js";import"./index-e8e7f773.js";import"./debounce-e2bc871f.js";/* empty css                    */import"./el-time-picker-f79f3178.js";import"./panel-time-pick-ae955f65.js";import"./index-da53d962.js";import"./isArrayLikeObject-724a27ce.js";import"./el-date-picker-2b967550.js";import"./el-input-number-f4eeab94.js";import"./el-select-46753b34.js";import"./index-6e9421a4.js";import"./selectAndShow.vue_vue_type_script_setup_true_lang-9eafc7ba.js";import"./employeesDialog-54936475.js";/* empty css                  */import"./selectBox-26a3a753.js";/* empty css                  *//* empty css                */import"./selectBox.vue_vue_type_style_index_0_scoped_82c14c6c_lang-f1c14f73.js";import"./index-85bc974f.js";import"./selectResult-4abbf083.js";/* empty css                                                                     *//* empty css                                                                        */import"./index-a6355ec2.js";import"./refs-5fd04da2.js";import"./orgItem-a67c9471.js";const j=["id"],q={__name:"conditionDrawer",setup(A){let t=m({conditionNodes:[]}),n=m({groupRelation:[]}),d=m(""),l=G(),{setCondition:L,setConditionsConfig:C}=l,c=s(()=>l.conditionsConfig1),N=s(()=>l.conditionDrawer),p=s({get(){return N.value},set(){u()}}),h=O();const k=s(()=>h.step2),D=()=>{};b(c,i=>{let o=i.value.conditionNodes;for(var r of o){let S=r.conditionList;for(var e of S){let V=e.conditionList;for(var a of V){let f=a.key;if(f==="root")a.keyType="SelectUser";else{let v=k.value.filter(U=>U.id===f);v.length>0&&(a.keyType=v[0].type)}}}}t.value=i.value,d.value=i.priorityLevel,n.value=i.priorityLevel?t.value.conditionNodes[i.priorityLevel-1]:{nodeUserList:[],conditionList:[]}}),B();const x=()=>{u();var i=t.value.conditionNodes.splice(d.value-1,1);t.value.conditionNodes.splice(n.value.priorityLevel-1,0,i[0]),t.value.conditionNodes.map((r,e)=>{r.priorityLevel=e+1});for(var o=0;o<t.value.conditionNodes.length;o++){let r=t.value.conditionNodes[o];r.error=!1,o!=t.value.conditionNodes.length-1&&(r.error=!w.checkCondition(t.value,o)),r.placeHolder=w.conditionStr(t.value,o)}C({value:t.value,flag:!0,id:c.value.id})},u=i=>{L(!1)};return(i,o)=>{const r=T;return E(),z(r,{"append-to-body":!0,title:"条件设置",modelValue:y(p),"onUpdate:modelValue":o[1]||(o[1]=e=>g(p)?p.value=e:p=e),"show-close":!1,size:550,onOpen:D,"before-close":x},{header:_(({titleId:e,titleClass:a})=>[F("h3",{id:e,class:I(a)},"条件设置",10,j)]),default:_(()=>[R(H,{data:y(n),"onUpdate:data":o[0]||(o[0]=e=>g(n)?n.value=e:n=e)},null,8,["data"])]),_:1},8,["modelValue"])}}},Ko=P(q,[["__scopeId","data-v-13643acc"]]);export{Ko as default};