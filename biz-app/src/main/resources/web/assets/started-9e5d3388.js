import{a as n,c as h,k as m,aH as L,d as F,r as p,K,o as Q,D as G,a5 as t,w as o,P as J,l as c,f as r,a3 as s,am as g,as as T,q as C,aD as O}from"./index-d5547550.js";import{v as W}from"./el-loading-7d43f325.js";import{E as X}from"./el-drawer-75c80c60.js";/* empty css                  */import{E as Y}from"./el-text-a7cd3acd.js";import{E as Z}from"./el-card-39a9c0d0.js";import{_ as tt}from"./index-bc35b8c7.js";import{E as et,a as ot}from"./el-table-column-ec0cff67.js";/* empty css                    */import"./el-tooltip-4ed993c7.js";import"./el-popper-00977718.js";import"./el-scrollbar-cbe0c54f.js";import{_ as rt,a as at}from"./ViewProcessInstanceImage.vue_vue_type_script_setup_true_lang-ccfa6d07.js";import{_ as it}from"./position-9c3f4805.js";import{E as st}from"./el-tag-de6752b8.js";import{_ as nt,a as lt}from"./refuse-b0e80fe1.js";import{_ as pt}from"./getFormWidget-6a62e076.js";import{_ as mt}from"./FlowNodeFormatData.vue_vue_type_script_setup_true_lang-2be44824.js";import{j as _t,k as ct}from"./index-8f215b9b.js";import{E as dt}from"./index-85bc974f.js";import{_ as ut}from"./_plugin-vue_export-helper-c27b6911.js";import"./use-dialog-27a1e524.js";import"./el-select-46753b34.js";import"./strings-fbdcd853.js";import"./isEqual-fe47b28a.js";import"./_Uint8Array-c7447712.js";import"./debounce-e2bc871f.js";import"./index-e8e7f773.js";import"./_baseClone-e7eb389f.js";import"./isArrayLikeObject-724a27ce.js";import"./_overRest-d3d6b67b.js";import"./_baseFlatten-c80c9ba3.js";import"./index-222e6800.js";import"./flatten-36990bb8.js";/* empty css                  */import"./index-a6355ec2.js";import"./refs-5fd04da2.js";import"./Area-836e6083.js";import"./area-23a735cb.js";import"./el-radio-92787871.js";import"./arrays-e667dc24.js";import"./cloneDeep-cfc40afc.js";import"./Date-3e356dbd.js";import"./el-date-picker-2b967550.js";import"./panel-time-pick-ae955f65.js";import"./index-da53d962.js";import"./DateTime-de06f21f.js";import"./Description-41c11d92.js";import"./Empty-e21e601f.js";import"./Input-45ba5ba8.js";import"./el-form-item-2de9c3c8.js";/* empty css                */import"./Money-ca211554.js";import"./el-input-number-f4eeab94.js";import"./MultiSelect-355b84fc.js";import"./Number-040385fc.js";import"./Score-a0ed8d55.js";import"./el-rate-1b1664df.js";import"./SelectDept-15a8e478.js";import"./selectAndShow.vue_vue_type_script_setup_true_lang-9eafc7ba.js";import"./employeesDialog-54936475.js";import"./selectBox-26a3a753.js";import"./selectBox.vue_vue_type_style_index_0_scoped_82c14c6c_lang-f1c14f73.js";import"./index-a7c7b111.js";import"./flow-165b4256.js";import"./const-a11e1fef.js";import"./selectResult-4abbf083.js";/* empty css                                                                     *//* empty css                                                                        */import"./orgItem-a67c9471.js";import"./SelectMultiDept-7fbc4ddc.js";import"./SelectMultiUser-a6f17dc4.js";import"./SelectUser-e4906c1f.js";import"./SingleSelect-915a5a36.js";import"./Textarea-466c5ec6.js";import"./Time-1bed9846.js";import"./el-time-picker-f79f3178.js";import"./UploadFile-b7fa1437.js";import"./el-progress-562499f2.js";import"./index-15f47425.js";import"./UploadImage-478fe7ac.js";import"./MultiUpload-acf0a282.js";import"./plus-bafa12a0.js";/* empty css                */import"./el-tab-pane-a6cb13f9.js";import"./el-divider-6edec4d7.js";const ft={viewBox:"0 0 1024 1024",width:"1.2em",height:"1.2em"},vt=m("path",{fill:"currentColor",d:"M224 448a32 32 0 0 0-32 32v384a32 32 0 0 0 32 32h576a32 32 0 0 0 32-32V480a32 32 0 0 0-32-32H224zm0-64h576a96 96 0 0 1 96 96v384a96 96 0 0 1-96 96H224a96 96 0 0 1-96-96V480a96 96 0 0 1 96-96z"},null,-1),gt=m("path",{fill:"currentColor",d:"M512 544a32 32 0 0 1 32 32v192a32 32 0 1 1-64 0V576a32 32 0 0 1 32-32zm192-160v-64a192 192 0 1 0-384 0v64h384zM512 64a256 256 0 0 1 256 256v128H256V320A256 256 0 0 1 512 64z"},null,-1),ht=[vt,gt];function yt(y,E){return n(),h("svg",ft,ht)}const wt={name:"ep-lock",render:yt};function kt(y){return L({url:"process-instance/detail",method:"get",params:y})}const bt={class:"app-container"},It={style:{position:"relative"}},xt={style:{display:"flex","flex-direction":"row"}},zt={class:"f11"},Ct={class:"f22"},Et={key:0,class:"iconclass",src:nt},Vt={key:1,class:"iconclass",src:lt},Dt=F({__name:"started",setup(y){function E(i){_t({processInstanceId:i.processInstanceId}).then(a=>{b()})}const v=p(!1),w=p(!1);p([]);const d=p(0),u=K({pageNum:1,pageSize:10}),V=p(),k=p(),$=i=>{k.value=i,kt({processInstanceId:i.processInstanceId}).then(a=>{l.value=a.data,v.value=!0})},l=p(),D=p(),R=i=>{D.value.view(i)};function b(){w.value=!0,ct(u).then(({data:i})=>{V.value=i.records,d.value=i.total}).finally(()=>{w.value=!1})}Q(()=>{b()});const B=G(()=>{var i={};for(var a of l.value.formItems)i[a.id]=a.props.value;return i});return(i,a)=>{const _=et,f=st,S=it,I=O,M=wt,U=at,q=ot,H=tt,x=Z,z=Y,P=dt,A=X,j=W;return n(),h("div",bt,[t(x,{shadow:"never"},{default:o(()=>[J((n(),c(q,{ref:"dataTableRef",data:r(V),"highlight-current-row":"",border:""},{default:o(()=>[t(_,{label:"分组",prop:"groupName",width:"100"}),t(_,{label:"流程",prop:"name",width:"150"}),t(_,{label:"发起时间",prop:"createTime",width:"200"}),t(_,{label:"结束时间",prop:"endTime",width:"200"}),t(_,{label:"状态",prop:"taskCreateTime",width:"150"},{default:o(e=>[e.row.status==1?(n(),c(f,{key:0,type:"success"},{default:o(()=>[s("进行中")]),_:1})):e.row.status==3?(n(),c(f,{key:1,type:"danger"},{default:o(()=>[s("已撤销")]),_:1})):(n(),c(f,{key:2},{default:o(()=>[s("已结束")]),_:1}))]),_:1}),t(_,{label:"审批结果",prop:"taskCreateTime",width:"150"},{default:o(e=>[e.row.result==1?(n(),c(f,{key:0,type:"success"},{default:o(()=>[s("同意")]),_:1})):e.row.result==2?(n(),c(f,{key:1,type:"danger"},{default:o(()=>[s("拒绝")]),_:1})):g("",!0)]),_:1}),t(_,{fixed:"right",label:"操作"},{default:o(e=>[t(I,{type:"primary",size:"small",link:"",onClick:N=>$(e.row)},{default:o(()=>[t(S),s(" 查看 ")]),_:2},1032,["onClick"]),t(I,{disabled:e.row.status!=1,type:"primary",size:"small",link:"",onClick:N=>E(e.row)},{default:o(()=>[t(M),s(" 撤销流程 ")]),_:2},1032,["disabled","onClick"]),t(I,{type:"primary",size:"small",link:"",onClick:N=>R(e.row)},{default:o(()=>[t(U),s(" 流程图 ")]),_:2},1032,["onClick"])]),_:1})]),_:1},8,["data"])),[[j,r(w)]]),r(d)>0?(n(),c(H,{key:0,total:r(d),"onUpdate:total":a[0]||(a[0]=e=>T(d)?d.value=e:null),page:r(u).pageNum,"onUpdate:page":a[1]||(a[1]=e=>r(u).pageNum=e),limit:r(u).pageSize,"onUpdate:limit":a[2]||(a[2]=e=>r(u).pageSize=e),onPagination:b},null,8,["total","page","limit"])):g("",!0)]),_:1}),t(A,{modelValue:r(v),"onUpdate:modelValue":a[3]||(a[3]=e=>T(v)?v.value=e:null),direction:"rtl",size:"400px"},{header:o(()=>[t(z,{size:"large",tag:"b",type:"info"},{default:o(()=>[s("流程详情")]),_:1})]),default:o(()=>[t(x,{style:{"margin-bottom":"20px"}},{default:o(()=>[m("div",It,[m("div",xt,[m("div",zt,[t(P,{shape:"square",size:50,src:r(l).starterAvatarUrl},{default:o(()=>[s(C(r(l).starterName.substring(0,1)),1)]),_:1},8,["src"])]),m("div",Ct,[m("div",null,[t(z,{tag:"b",size:"large",type:"primary"},{default:o(()=>{var e;return[s(C((e=r(l))==null?void 0:e.processName),1)]}),_:1})]),m("div",null,[t(z,{size:"small"},{default:o(()=>[s(C(r(l).startTime),1)]),_:1})])])]),r(l).processInstanceResult==1?(n(),h("img",Et)):g("",!0),r(l).processInstanceResult==2?(n(),h("img",Vt)):g("",!0)])]),_:1}),t(x,{class:"box-card"},{default:o(()=>[t(pt,{ref:"formRenderRef","form-list":r(l).formItems},null,8,["form-list"])]),_:1}),t(mt,{disableSelect:!0,formData:r(B),processInstanceId:r(k).processInstanceId,"flow-id":r(k).flowId,ref:"flowNodeFormatRef"},null,8,["formData","processInstanceId","flow-id"])]),_:1},8,["modelValue"]),t(rt,{ref_key:"viewImageRef",ref:D},null,512)])}}});const lo=ut(Dt,[["__scopeId","data-v-2413df2f"]]);export{lo as default};