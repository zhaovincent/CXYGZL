import{d as ce,r as _,K as E,D as fe,o as ge,a as d,c as ve,k as D,a5 as e,w as o,f as a,ay as ye,a3 as r,P as j,l as c,q as L,as as A,am as be,a7 as V,an as ke,eq as he,aA as Ee,aB as Ve}from"./index-e01dade3.js";import{v as we}from"./el-loading-b076b1a7.js";import{E as Ce}from"./el-scrollbar-f2029ad1.js";import{E as Q}from"./el-tree-5fcea156.js";import"./el-checkbox-123bf6dd.js";import{E as Re}from"./el-dialog-1471a1af.js";import{E as Se}from"./el-input-number-182b17d2.js";import{E as xe,a as De}from"./el-radio-70657465.js";import{E as Ie}from"./el-tag-929563f7.js";import{E as Me,a as Ue}from"./el-select-c154265f.js";import"./el-popper-ee5283d6.js";import{E as Ne}from"./el-card-16bdf0d8.js";import{_ as qe}from"./index-1cadfa40.js";import{E as Be,a as Fe}from"./el-table-column-7cf019e1.js";import"./el-tooltip-4ed993c7.js";import{_ as Te,a as ze,b as $e}from"./search-93a6394a.js";import{_ as Ke}from"./edit-26548300.js";import{_ as Oe}from"./position-f826c78d.js";import{_ as Pe}from"./plus-95522b44.js";/* empty css                */import{E as I,a as je}from"./el-form-item-85be6a3a.js";import{g as Le,u as Ae,a as Qe,d as Ge,b as He,c as Je}from"./index-227556bc.js";import"./index-af3dbd4b.js";import"./isEqual-47390e81.js";import"./_Uint8Array-aeb50f83.js";import"./flatten-178b99bc.js";import"./_baseFlatten-6e5398bc.js";import"./_overRest-a79e0547.js";import"./refs-dd65b3fd.js";import"./index-f7c59abe.js";import"./strings-6464b592.js";import"./debounce-bf37b7f7.js";import"./index-6b517de5.js";import"./_plugin-vue_export-helper-c27b6911.js";import"./_baseClone-59dac119.js";import"./isArrayLikeObject-a5301880.js";const We={class:"app-container"},Xe={class:"search"},Ye={class:"dialog-footer"},Ze={class:"dialog-footer"},Tt=ce({name:"Role",inheritAttrs:!1,__name:"index",setup(et){const M=_(I),w=_(I),S=_(Q),i=_(!1),U=_([]),b=_(0),m=E({pageNum:1,pageSize:10}),N=_(),g=E({visible:!1}),s=E({sort:1,status:1,key:"",name:""}),G=E({name:[{required:!0,message:"请输入角色名称",trigger:"blur"}],key:[{required:!0,message:"请输入角色编码",trigger:"blur"}],dataScope:[{required:!0,message:"请选择数据权限",trigger:"blur"}],status:[{required:!0,message:"请选择状态",trigger:"blur"}]}),y=_(!1),q=_([]),H=fe(()=>function(n){return n==0?"全部数据":n==1?"部门以及子部门数据":n==2?"本部门数据":n==3?"本人数据":""});let x=E({});function k(){i.value=!0,Le(m).then(({data:n})=>{N.value=n.records,b.value=n.total}).finally(()=>{i.value=!1})}function h(){M.value.resetFields(),m.pageNum=1,k()}function J(n){U.value=n.map(t=>t.id)}function B(n){g.visible=!0,n.id?(g.title="修改角色",Object.assign(s,n)):(g.title="新增角色",Object.assign(s,{}))}function W(){i.value=!0,w.value.validate(n=>{if(n){const t=s.id;t?Ae(t,s).then(()=>{V.success("修改成功"),C(),h()}).finally(()=>i.value=!1):Qe(s).then(()=>{V.success("新增成功"),C(),h()}).finally(()=>i.value=!1)}})}function C(){g.visible=!1,X()}function X(){w.value.resetFields(),w.value.clearValidate(),s.id=void 0,s.sort=1,s.status=1}function Y(n){const t=[n||U.value].join(",");if(!t){V.warning("请勾选删除项");return}ke.confirm("确认删除已选中的数据项?","警告",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(()=>{i.value=!0,Ge(t).then(()=>{V.success("删除成功"),h()}).finally(()=>i.value=!1)})}function Z(n){const t=n.id;t&&(x={id:t,name:n.name},y.value=!0,i.value=!0,he().then(f=>{q.value=f.data,He(t).then(({data:p})=>{p.forEach(u=>S.value.setChecked(u,!0,!1))}).finally(()=>{i.value=!1})}))}function ee(){const n=x.id;if(n){const t=S.value.getCheckedNodes(!1,!0).map(f=>f.id);i.value=!0,Je(n,t).then(f=>{V.success("分配权限成功"),y.value=!1,h()}).finally(()=>{i.value=!1})}}return ge(()=>{k()}),(n,t)=>{const f=Ee,p=je,F=Te,u=Ve,te=ze,T=I,oe=Pe,v=Be,z=Ie,le=Oe,ae=Ke,ne=$e,se=Fe,re=qe,ie=Ne,R=Me,ue=Ue,$=xe,de=De,me=Se,K=Re,pe=Q,_e=Ce,O=we;return d(),ve("div",We,[D("div",Xe,[e(T,{ref_key:"queryFormRef",ref:M,model:a(m),inline:!0},{default:o(()=>[e(p,{prop:"keywords",label:"关键字"},{default:o(()=>[e(f,{modelValue:a(m).keywords,"onUpdate:modelValue":t[0]||(t[0]=l=>a(m).keywords=l),placeholder:"角色名称",clearable:"",onKeyup:ye(k,["enter"])},null,8,["modelValue","onKeyup"])]),_:1}),e(p,null,{default:o(()=>[e(u,{type:"primary",onClick:k},{default:o(()=>[e(F),r("搜索")]),_:1}),e(u,{onClick:h},{default:o(()=>[e(te),r("重置")]),_:1})]),_:1})]),_:1},8,["model"])]),e(ie,{shadow:"never"},{header:o(()=>[e(u,{type:"success",onClick:t[1]||(t[1]=l=>B({}))},{default:o(()=>[e(oe),r("新增")]),_:1})]),default:o(()=>[j((d(),c(se,{ref:"dataTableRef",data:a(N),"highlight-current-row":"",border:"",onSelectionChange:J},{default:o(()=>[e(v,{type:"selection",width:"55",align:"center"}),e(v,{label:"角色名称",prop:"name","min-width":"100"}),e(v,{label:"角色编码",prop:"key",width:"150"}),e(v,{label:"数据权限",prop:"dataScope",width:"250"},{default:o(l=>[r(L(H.value(l.row.dataScope)),1)]),_:1}),e(v,{label:"状态",align:"center",width:"100"},{default:o(l=>[l.row.status===1?(d(),c(z,{key:0,type:"success"},{default:o(()=>[r("正常")]),_:1})):(d(),c(z,{key:1,type:"info"},{default:o(()=>[r("禁用")]),_:1}))]),_:1}),e(v,{label:"排序",align:"center",width:"80",prop:"sort"}),e(v,{fixed:"right",label:"操作",width:"220"},{default:o(l=>[e(u,{type:"primary",size:"small",link:"",onClick:P=>Z(l.row)},{default:o(()=>[e(le),r("分配权限 ")]),_:2},1032,["onClick"]),e(u,{type:"primary",size:"small",link:"",onClick:P=>B(l.row)},{default:o(()=>[e(ae),r("编辑 ")]),_:2},1032,["onClick"]),e(u,{type:"primary",size:"small",link:"",onClick:P=>Y(l.row.id)},{default:o(()=>[e(ne),r("删除 ")]),_:2},1032,["onClick"])]),_:1})]),_:1},8,["data"])),[[O,a(i)]]),a(b)>0?(d(),c(re,{key:0,total:a(b),"onUpdate:total":t[2]||(t[2]=l=>A(b)?b.value=l:null),page:a(m).pageNum,"onUpdate:page":t[3]||(t[3]=l=>a(m).pageNum=l),limit:a(m).pageSize,"onUpdate:limit":t[4]||(t[4]=l=>a(m).pageSize=l),onPagination:k},null,8,["total","page","limit"])):be("",!0)]),_:1}),e(K,{modelValue:a(g).visible,"onUpdate:modelValue":t[10]||(t[10]=l=>a(g).visible=l),title:a(g).title,width:"500px",onClose:C},{footer:o(()=>[D("div",Ye,[e(u,{type:"primary",onClick:W},{default:o(()=>[r("确 定")]),_:1}),e(u,{onClick:C},{default:o(()=>[r("取 消")]),_:1})])]),default:o(()=>[e(T,{ref_key:"roleFormRef",ref:w,model:a(s),rules:a(G),"label-width":"100px"},{default:o(()=>[e(p,{label:"角色名称",prop:"name"},{default:o(()=>[e(f,{modelValue:a(s).name,"onUpdate:modelValue":t[5]||(t[5]=l=>a(s).name=l),placeholder:"请输入角色名称"},null,8,["modelValue"])]),_:1}),e(p,{label:"角色编码",prop:"key"},{default:o(()=>[e(f,{modelValue:a(s).key,"onUpdate:modelValue":t[6]||(t[6]=l=>a(s).key=l),placeholder:"请输入角色编码"},null,8,["modelValue"])]),_:1}),e(p,{label:"数据权限",prop:"dataScope"},{default:o(()=>[e(ue,{modelValue:a(s).dataScope,"onUpdate:modelValue":t[7]||(t[7]=l=>a(s).dataScope=l)},{default:o(()=>[(d(),c(R,{key:0,label:"全部数据",value:0})),(d(),c(R,{key:1,label:"部门及子部门数据",value:1})),(d(),c(R,{key:2,label:"本部门数据",value:2})),(d(),c(R,{key:3,label:"本人数据",value:3}))]),_:1},8,["modelValue"])]),_:1}),e(p,{label:"状态",prop:"status"},{default:o(()=>[e(de,{modelValue:a(s).status,"onUpdate:modelValue":t[8]||(t[8]=l=>a(s).status=l)},{default:o(()=>[e($,{label:1},{default:o(()=>[r("正常")]),_:1}),e($,{label:0},{default:o(()=>[r("停用")]),_:1})]),_:1},8,["modelValue"])]),_:1}),e(p,{label:"排序",prop:"sort"},{default:o(()=>[e(me,{modelValue:a(s).sort,"onUpdate:modelValue":t[9]||(t[9]=l=>a(s).sort=l),"controls-position":"right",min:0,style:{width:"100px"}},null,8,["modelValue"])]),_:1})]),_:1},8,["model","rules"])]),_:1},8,["modelValue","title"]),e(K,{modelValue:a(y),"onUpdate:modelValue":t[12]||(t[12]=l=>A(y)?y.value=l:null),title:"【"+a(x).name+"】权限分配",width:"800px"},{footer:o(()=>[D("div",Ze,[e(u,{type:"primary",onClick:ee},{default:o(()=>[r("确 定")]),_:1}),e(u,{onClick:t[11]||(t[11]=l=>y.value=!1)},{default:o(()=>[r("取 消")]),_:1})])]),default:o(()=>[j((d(),c(_e,{"max-height":"600px"},{default:o(()=>[e(pe,{ref_key:"menuRef",ref:S,"node-key":"id","show-checkbox":"",data:a(q),"default-expand-all":!0},{default:o(({data:l})=>[r(L(l.name),1)]),_:1},8,["data"])]),_:1})),[[O,a(i)]])]),_:1},8,["modelValue","title"])])}}});export{Tt as default};
