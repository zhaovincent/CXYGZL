import{d as re,r as V,K as k,o as ie,ca as de,a as i,c as se,k as R,a5 as a,w as l,f as t,ay as pe,a3 as r,P as w,l as d,q as ue,am as m,ae as A,aK as me,ep as _e,eq as fe,er as ce,a7 as E,es as ye,an as be,et as ve,aA as ge,aB as he}from"./index-e01dade3.js";import{v as Ve}from"./el-loading-b076b1a7.js";import{E as ke}from"./el-dialog-1471a1af.js";import{E as we}from"./el-input-number-182b17d2.js";import{E as Ee,a as Te}from"./el-radio-70657465.js";import{E as Ue}from"./el-tag-929563f7.js";import"./el-select-c154265f.js";import"./el-scrollbar-f2029ad1.js";import"./el-popper-ee5283d6.js";import"./el-tree-5fcea156.js";import"./el-checkbox-123bf6dd.js";import{E as Ce}from"./el-tree-select-af3ba868.js";import{E as Ne}from"./el-card-16bdf0d8.js";import{E as Me,a as Ie}from"./el-table-column-7cf019e1.js";import"./el-tooltip-4ed993c7.js";import{_ as Oe,a as Ae,b as Be}from"./search-93a6394a.js";import{_ as xe}from"./edit-26548300.js";import{_ as Le}from"./plus-95522b44.js";/* empty css                */import{E as B,a as qe}from"./el-form-item-85be6a3a.js";import{S as Fe}from"./index-feab0d37.js";import{I as Ke}from"./index-660d987d.js";import"./refs-dd65b3fd.js";import"./index-f7c59abe.js";import"./strings-6464b592.js";import"./isEqual-47390e81.js";import"./_Uint8Array-aeb50f83.js";import"./debounce-bf37b7f7.js";import"./index-6b517de5.js";import"./index-af3dbd4b.js";import"./flatten-178b99bc.js";import"./_baseFlatten-6e5398bc.js";import"./_overRest-a79e0547.js";import"./_baseClone-59dac119.js";import"./isArrayLikeObject-a5301880.js";import"./_plugin-vue_export-helper-c27b6911.js";import"./el-popover-70136418.js";import"./dropdown-b5481c97.js";import"./el-divider-84ed2554.js";import"./caret-bottom-57d19efe.js";var p=(v=>(v.CATALOG="CATALOG",v.MENU="MENU",v.BUTTON="BUTTON",v.EXTLINK="EXTLINK",v))(p||{});const De={class:"app-container"},Ge={class:"search"},Re={class:"dialog-footer"},Nt=re({name:"Menu",inheritAttrs:!1,__name:"index",setup(v){const x=V(B),T=V(B),I=V(!1),y=k({visible:!1}),U=k({}),L=V([]),q=V([]),e=k({parentId:void 0,visible:1,sort:1,type:p.MENU}),S=k({parentId:[{required:!0,message:"请选择父级菜单",trigger:"blur"}],name:[{required:!0,message:"请输入菜单名称",trigger:"blur"}],type:[{required:!0,message:"请选择菜单类型",trigger:"blur"}],path:[{required:!0,message:"请输入路由路径",trigger:"blur"}],component:[{required:!0,message:"请输入组件完整路径",trigger:"blur"}]}),z=V(),C=k({type:"",path:""});function b(){I.value=!0,_e(U).then(({data:_})=>{L.value=_}).then(()=>{I.value=!1})}function X(){x.value.resetFields(),b()}function P(_){z.value=_.id}function O(_,n,u){fe().then(({data:s})=>{q.value=[{id:0,name:"顶级菜单",children:s}]}).then(()=>{y.visible=!0,n?(y.title="编辑菜单",Object.assign(e,u),C.type=u.type,C.path=u.path??""):(y.title="新增菜单",e.id=void 0,e.visible=1,e.sort=1,e.type="MENU",e.perm=void 0,e.name=void 0,e.component=void 0,e.path=void 0,e.parentId=_)})}function $(){e.type!==C.type?e.path="":e.path=C.path}function Q(){T.value.validate(_=>{if(_){const n=e.id;n?ce(n,e).then(()=>{E.success("修改成功"),N(),b()}):ye(e).then(()=>{E.success("新增成功"),N(),b()})}})}function j(_){if(!_)return E.warning("请勾选删除项"),!1;be.confirm("确认删除已选中的数据项?","警告",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(()=>{ve(_).then(()=>{E.success("删除成功"),b()})}).catch(()=>E.info("已取消删除"))}function N(){y.visible=!1,H()}function H(){T.value.resetFields(),T.value.clearValidate(),e.id=void 0,e.parentId=void 0,e.visible=1,e.sort=1,e.perm=void 0,e.component=void 0,e.path=void 0}return ie(()=>{b()}),(_,n)=>{const u=ge,s=qe,J=Oe,f=he,W=Ae,F=B,K=Le,c=Me,g=Ue,Y=xe,Z=Be,ee=Ie,te=Ne,le=Ce,h=Ee,D=Te,oe=we,ae=ke,M=de("hasPerm"),ne=Ve;return i(),se("div",De,[R("div",Ge,[a(F,{ref_key:"queryFormRef",ref:x,model:t(U),inline:!0},{default:l(()=>[a(s,{label:"关键字",prop:"keywords"},{default:l(()=>[a(u,{modelValue:t(U).keywords,"onUpdate:modelValue":n[0]||(n[0]=o=>t(U).keywords=o),placeholder:"菜单名称",clearable:"",onKeyup:pe(b,["enter"])},null,8,["modelValue","onKeyup"])]),_:1}),a(s,null,{default:l(()=>[a(f,{type:"primary",onClick:b},{icon:l(()=>[a(J)]),default:l(()=>[r(" 搜索 ")]),_:1}),a(f,{onClick:X},{icon:l(()=>[a(W)]),default:l(()=>[r(" 重置 ")]),_:1})]),_:1})]),_:1},8,["model"])]),a(te,{shadow:"never",size:"small"},{header:l(()=>[w((i(),d(f,{type:"success",onClick:n[1]||(n[1]=o=>O(0))},{icon:l(()=>[a(K)]),default:l(()=>[r(" 新增 ")]),_:1})),[[M,["sys:menu:add"]]])]),default:l(()=>[w((i(),d(ee,{data:t(L),"highlight-current-row":"","tree-props":{children:"children",hasChildren:"hasChildren"},"row-key":"id","default-expand-all":"",border:"",size:"small",onRowClick:P},{default:l(()=>[a(c,{label:"菜单名称","min-width":"200"},{default:l(o=>[a(Fe,{"icon-class":o.row.type===t(p).BUTTON?"button":o.row.icon},null,8,["icon-class"]),r(" "+ue(o.row.name),1)]),_:1}),a(c,{label:"类型",align:"center",width:"80"},{default:l(o=>[o.row.type===t(p).CATALOG?(i(),d(g,{key:0,type:"warning"},{default:l(()=>[r("目录 ")]),_:1})):m("",!0),o.row.type===t(p).MENU?(i(),d(g,{key:1,type:"success"},{default:l(()=>[r("菜单 ")]),_:1})):m("",!0),o.row.type===t(p).BUTTON?(i(),d(g,{key:2,type:"danger"},{default:l(()=>[r("按钮 ")]),_:1})):m("",!0),o.row.type===t(p).EXTLINK?(i(),d(g,{key:3,type:"info"},{default:l(()=>[r("外链 ")]),_:1})):m("",!0)]),_:1}),a(c,{label:"路由路径",align:"left",width:"150",prop:"path"}),a(c,{label:"组件路径",align:"left",width:"250",prop:"component"}),a(c,{label:"权限标识",align:"center",width:"200",prop:"perm"}),a(c,{label:"状态",align:"center",width:"80"},{default:l(o=>[o.row.visible===1?(i(),d(g,{key:0,type:"success"},{default:l(()=>[r("显示")]),_:1})):(i(),d(g,{key:1,type:"info"},{default:l(()=>[r("隐藏")]),_:1}))]),_:1}),a(c,{label:"排序",align:"center",width:"80",prop:"sort"}),a(c,{fixed:"right",align:"center",label:"操作",width:"220"},{default:l(o=>[o.row.type=="CATALOG"||o.row.type=="MENU"?w((i(),d(f,{key:0,type:"primary",link:"",size:"small",onClick:A(G=>O(o.row.id),["stop"])},{default:l(()=>[a(K),r(" 新增 ")]),_:2},1032,["onClick"])),[[M,["sys:menu:add"]]]):m("",!0),w((i(),d(f,{type:"primary",link:"",size:"small",onClick:A(G=>O(void 0,o.row.id,o.row),["stop"])},{default:l(()=>[a(Y),r(" 编辑 ")]),_:2},1032,["onClick"])),[[M,["sys:menu:edit"]]]),w((i(),d(f,{type:"primary",link:"",size:"small",onClick:A(G=>j(o.row.id),["stop"])},{default:l(()=>[a(Z),r(" 删除 ")]),_:2},1032,["onClick"])),[[M,["sys:menu:delete"]]])]),_:1})]),_:1},8,["data"])),[[ne,t(I)]])]),_:1}),a(ae,{modelValue:t(y).visible,"onUpdate:modelValue":n[14]||(n[14]=o=>t(y).visible=o),title:t(y).title,"destroy-on-close":"","append-to-body":"",width:"750px",onClose:N},{footer:l(()=>[R("div",Re,[a(f,{type:"primary",onClick:Q},{default:l(()=>[r("确 定")]),_:1}),a(f,{onClick:N},{default:l(()=>[r("取 消")]),_:1})])]),default:l(()=>[a(F,{ref_key:"menuFormRef",ref:T,model:t(e),rules:t(S),"label-width":"100px"},{default:l(()=>[a(s,{label:"父级菜单",prop:"parentId"},{default:l(()=>[a(le,{modelValue:t(e).parentId,"onUpdate:modelValue":n[2]||(n[2]=o=>t(e).parentId=o),placeholder:"选择上级菜单",data:t(q),filterable:"",props:{label:"name",value:"id"},"check-strictly":"","render-after-expand":!1},null,8,["modelValue","data"])]),_:1}),a(s,{label:"菜单名称",prop:"name"},{default:l(()=>[a(u,{modelValue:t(e).name,"onUpdate:modelValue":n[3]||(n[3]=o=>t(e).name=o),placeholder:"请输入菜单名称"},null,8,["modelValue"])]),_:1}),a(s,{label:"菜单类型",prop:"type"},{default:l(()=>[a(D,{modelValue:t(e).type,"onUpdate:modelValue":n[4]||(n[4]=o=>t(e).type=o),onChange:$},{default:l(()=>[a(h,{label:"CATALOG"},{default:l(()=>[r("目录")]),_:1}),a(h,{label:"MENU"},{default:l(()=>[r("菜单")]),_:1}),a(h,{label:"BUTTON"},{default:l(()=>[r("按钮")]),_:1}),a(h,{label:"EXTLINK"},{default:l(()=>[r("外链")]),_:1})]),_:1},8,["modelValue"])]),_:1}),t(e).type=="EXTLINK"?(i(),d(s,{key:0,label:"外链地址",prop:"path"},{default:l(()=>[a(u,{modelValue:t(e).path,"onUpdate:modelValue":n[5]||(n[5]=o=>t(e).path=o),placeholder:"请输入外链完整路径"},null,8,["modelValue"])]),_:1})):m("",!0),t(e).type==t(p).CATALOG||t(e).type==t(p).MENU?(i(),d(s,{key:1,label:"路由路径",prop:"path"},{default:l(()=>[t(e).type==t(p).CATALOG?(i(),d(u,{key:0,modelValue:t(e).path,"onUpdate:modelValue":n[6]||(n[6]=o=>t(e).path=o),placeholder:"system"},null,8,["modelValue"])):(i(),d(u,{key:1,modelValue:t(e).path,"onUpdate:modelValue":n[7]||(n[7]=o=>t(e).path=o),placeholder:"user"},null,8,["modelValue"]))]),_:1})):m("",!0),t(e).type==t(p).MENU?(i(),d(s,{key:2,label:"页面路径",prop:"component"},{default:l(()=>[a(u,{modelValue:t(e).component,"onUpdate:modelValue":n[8]||(n[8]=o=>t(e).component=o),placeholder:"system/user/index",style:{width:"95%"}},me({_:2},[t(e).type==t(p).MENU?{name:"prepend",fn:l(()=>[r("src/views/ ")]),key:"0"}:void 0,t(e).type==t(p).MENU?{name:"append",fn:l(()=>[r(".vue ")]),key:"1"}:void 0]),1032,["modelValue"])]),_:1})):m("",!0),t(e).type=="BUTTON"?(i(),d(s,{key:3,label:"权限标识",prop:"perm"},{default:l(()=>[a(u,{modelValue:t(e).perm,"onUpdate:modelValue":n[9]||(n[9]=o=>t(e).perm=o),placeholder:"sys:user:add"},null,8,["modelValue"])]),_:1})):m("",!0),t(e).type!=="BUTTON"?(i(),d(s,{key:4,label:"图标",prop:"icon"},{default:l(()=>[a(Ke,{modelValue:t(e).icon,"onUpdate:modelValue":n[10]||(n[10]=o=>t(e).icon=o)},null,8,["modelValue"])]),_:1})):m("",!0),t(e).type==t(p).CATALOG?(i(),d(s,{key:5,label:"跳转路由"},{default:l(()=>[a(u,{modelValue:t(e).redirect,"onUpdate:modelValue":n[11]||(n[11]=o=>t(e).redirect=o),placeholder:"跳转路由"},null,8,["modelValue"])]),_:1})):m("",!0),t(e).type!=="BUTTON"?(i(),d(s,{key:6,label:"状态"},{default:l(()=>[a(D,{modelValue:t(e).visible,"onUpdate:modelValue":n[12]||(n[12]=o=>t(e).visible=o)},{default:l(()=>[a(h,{label:1},{default:l(()=>[r("显示")]),_:1}),a(h,{label:0},{default:l(()=>[r("隐藏")]),_:1})]),_:1},8,["modelValue"])]),_:1})):m("",!0),a(s,{label:"排序",prop:"sort"},{default:l(()=>[a(oe,{modelValue:t(e).sort,"onUpdate:modelValue":n[13]||(n[13]=o=>t(e).sort=o),style:{width:"100px"},"controls-position":"right",min:0},null,8,["modelValue"])]),_:1})]),_:1},8,["model","rules"])]),_:1},8,["modelValue","title"])])}}});export{Nt as default};