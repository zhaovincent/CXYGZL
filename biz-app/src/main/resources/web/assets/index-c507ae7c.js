import{a as u,c as R,k as _,d as ze,r as d,K as k,U as $e,D as Re,o as qe,ca as Ne,a5 as e,w as a,f as o,as as P,ay as Ae,a3 as r,P as C,l as g,q as oe,am as Le,R as Pe,a6 as Oe,an as O,eu as Me,a7 as c,ev as Ke,ew as je,ex as He,ey as Xe,ez as Ze,eA as Ge,aA as Qe,aB as Je,E as We,eB as Ye,eC as et}from"./index-e01dade3.js";import{v as tt}from"./el-loading-b076b1a7.js";import{E as lt}from"./el-progress-be452bc9.js";/* empty css                */import{E as at}from"./el-dialog-1471a1af.js";import{E as ot,a as nt}from"./el-radio-70657465.js";import"./el-tag-929563f7.js";import{E as st,a as rt}from"./el-select-c154265f.js";import"./el-scrollbar-f2029ad1.js";import"./el-popper-ee5283d6.js";import{E as ne}from"./el-tree-5fcea156.js";import"./el-checkbox-123bf6dd.js";import{E as it}from"./el-tree-select-af3ba868.js";import{a as dt,E as ut}from"./el-row-e9611a44.js";import{_ as pt}from"./index-1cadfa40.js";import{E as mt,a as ct}from"./el-table-column-7cf019e1.js";import"./el-tooltip-4ed993c7.js";import{_ as _t,a as ft,b as gt}from"./search-93a6394a.js";import{_ as vt}from"./edit-26548300.js";import{E as ht}from"./el-switch-e9147eb4.js";import{E as bt}from"./el-avatar-63a3488d.js";import{_ as wt}from"./plus-95522b44.js";/* empty css                */import{E as M,a as yt}from"./el-form-item-85be6a3a.js";import{E as xt}from"./el-card-16bdf0d8.js";import{S as Vt}from"./SingleUpload-dfe85bb8.js";import{l as Et}from"./index-8f0c79b2.js";import{l as Ut}from"./index-227556bc.js";import"./_baseClone-59dac119.js";import"./_Uint8Array-aeb50f83.js";import"./isEqual-47390e81.js";import"./refs-dd65b3fd.js";import"./strings-6464b592.js";import"./debounce-bf37b7f7.js";import"./index-6b517de5.js";import"./index-af3dbd4b.js";import"./flatten-178b99bc.js";import"./_baseFlatten-6e5398bc.js";import"./_overRest-a79e0547.js";import"./_plugin-vue_export-helper-c27b6911.js";import"./isArrayLikeObject-a5301880.js";import"./index-f7c9196e.js";const kt={viewBox:"0 0 1024 1024",width:"1.2em",height:"1.2em"},Ct=_("path",{fill:"currentColor",d:"M544 864V672h128L512 480L352 672h128v192H320v-1.6c-5.376.32-10.496 1.6-16 1.6A240 240 0 0 1 64 624c0-123.136 93.12-223.488 212.608-237.248A239.808 239.808 0 0 1 512 192a239.872 239.872 0 0 1 235.456 194.752c119.488 13.76 212.48 114.112 212.48 237.248a240 240 0 0 1-240 240c-5.376 0-10.56-1.28-16-1.6v1.6H544z"},null,-1),It=[Ct];function Dt(K,I){return u(),R("svg",kt,It)}const St={name:"ep-upload-filled",render:Dt},Bt={viewBox:"0 0 1024 1024",width:"1.2em",height:"1.2em"},Ft=_("path",{fill:"currentColor",d:"M289.088 296.704h92.992a32 32 0 0 1 0 64H232.96a32 32 0 0 1-32-32V179.712a32 32 0 0 1 64 0v50.56a384 384 0 0 1 643.84 282.88a384 384 0 0 1-383.936 384a384 384 0 0 1-384-384h64a320 320 0 1 0 640 0a320 320 0 0 0-555.712-216.448z"},null,-1),Tt=[Ft];function zt(K,I){return u(),R("svg",Bt,Tt)}const $t={name:"ep-refresh-left",render:zt},Rt={class:"app-container"},qt={class:"search"},Nt={class:"flex justify-between"},At={class:"dialog-footer"},Lt=_("div",{class:"el-upload__text"},[r(" 将文件拖到此处，或 "),_("em",null,"点击上传")],-1),Pt=_("div",{class:"el-upload__tip"},"xls/xlsx files",-1),Ot={class:"dialog-footer"},Bl=ze({name:"User",inheritAttrs:!1,__name:"index",setup(K){const I=d(ne),j=d(M),D=d(M),h=d(!1),H=d([]),w=d(0),v=k({visible:!1}),i=k({pageNum:1,pageSize:10}),X=d(),s=k({status:1}),se=k({name:[{required:!0,message:"姓名不能为空",trigger:"blur"}],avatarUrl:[{required:!0,message:"请上传头像",trigger:"blur"}],deptId:[{required:!0,message:"所属部门不能为空",trigger:"blur"}],roleIds:[{required:!0,message:"用户角色不能为空",trigger:"blur"}],gender:[{required:!0,message:"性别不能为空",trigger:"blur"}],email:[{pattern:/\w[-\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\.)+[A-Za-z]{2,14}/,message:"请输入正确的邮箱地址",trigger:"blur"}],phone:[{required:!0,message:"手机号不能为空",trigger:"blur"},{pattern:/^1[3|4|5|6|7|8|9][0-9]\d{8}$/,message:"请输入正确的手机号码",trigger:"blur"}]}),S=d(),B=d(),Z=d(),F=k({title:"用户导入",visible:!1}),y=d(0),x=d(),q=d([]);$e(()=>{I.value.filter(S.value)},{flush:"post"});function re(n,t){return n?t.label.indexOf(n)!==-1:!0}function ie(n){i.deptId=n.id,b()}async function de(){Ut().then(n=>{Z.value=n.data})}function ue(n){const t=n.status===1?"启用":"停用";O.confirm("确认要"+t+n.phone+"用户吗?","警告",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(()=>Me(n.id,n.status)).then(()=>{c.success(t+"成功")}).catch(()=>{n.status=n.status===1?0:1})}function b(){h.value=!0,Ke(i).then(({data:n})=>{X.value=n.records,w.value=n.total}).finally(()=>{h.value=!1})}function V(){j.value.resetFields(),i.pageNum=1,i.deptId=void 0,b()}function pe(n){H.value=n.map(t=>t.id)}function me(n){O.prompt("请输入用户「"+n.phone+"」的新密码","重置密码",{confirmButtonText:"确定",cancelButtonText:"取消"}).then(({value:t})=>{if(!t)return c.warning("请输入新密码"),!1;je(n.id,t).then(()=>{c.success("密码重置成功，新密码是："+t)})}).catch(()=>{})}async function G(n){await Q(),await de(),v.visible=!0,n?(v.title="修改用户",He(n).then(({data:t})=>{t.deptId=""+t.deptId,Object.assign(s,t)})):v.title="新增用户"}function T(){v.visible=!1,ce()}function ce(){D.value.resetFields(),D.value.clearValidate(),s.id=void 0,s.status=1}const _e=Xe(()=>{D.value.validate(n=>{if(n){const t=s.id;h.value=!0,t?Ye(t,s).then(()=>{c.success("修改用户成功"),T(),V()}).finally(()=>h.value=!1):et(s).then(()=>{c.success("新增用户成功"),T(),V()}).finally(()=>h.value=!1)}})},3e3);function fe(n){const t=[n||H.value].join(",");if(!t){c.warning("请勾选删除项");return}O.confirm("确认删除用户?","警告",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(function(){Ze(t).then(()=>{c.success("删除成功"),V()})})}async function Q(){Et().then(n=>{B.value=n.data})}const ge=Re(()=>function(n){return n==1?"男":n==2?"女":""});function ve(n){if(!/\.(xlsx|xls|XLSX|XLS)$/.test(n.name))return c.warning("上传Excel只能为xlsx、xls格式"),x.value=void 0,q.value=[],!1;x.value=n.raw}function he(){if(y.value){if(!x.value)return c.warning("上传Excel文件不能为空"),!1;Ge(y.value,x.value).then(n=>{c.success(n.data),N(),V()})}}function N(){F.visible=!1,x.value=void 0,q.value=[]}return qe(()=>{Q(),b()}),(n,t)=>{const J=_t,z=Qe,be=ne,W=xt,Y=dt,p=yt,E=st,A=rt,m=Je,we=ft,L=M,ye=wt,f=mt,ee=bt,xe=ht,Ve=$t,Ee=vt,Ue=gt,ke=ct,Ce=pt,Ie=ut,te=it,le=ot,De=nt,ae=at,Se=St,Be=We,Fe=lt,$=Ne("hasPerm"),Te=tt;return u(),R("div",Rt,[e(Ie,{gutter:20},{default:a(()=>[e(Y,{lg:4,xs:24,class:"mb-[12px]"},{default:a(()=>[e(W,{shadow:"never"},{default:a(()=>[e(z,{modelValue:o(S),"onUpdate:modelValue":t[0]||(t[0]=l=>P(S)?S.value=l:null),placeholder:"部门名称",clearable:""},{prefix:a(()=>[e(J)]),_:1},8,["modelValue"]),e(be,{ref_key:"deptTreeRef",ref:I,class:"mt-2",data:o(B),props:{children:"children",value:"id",label:"name",disabled:""},"expand-on-click-node":!1,"filter-node-method":re,"default-expand-all":"",onNodeClick:ie},null,8,["data"])]),_:1})]),_:1}),e(Y,{lg:20,xs:24},{default:a(()=>[_("div",qt,[e(L,{ref_key:"queryFormRef",ref:j,model:o(i),inline:!0},{default:a(()=>[e(p,{label:"关键字",prop:"keywords"},{default:a(()=>[e(z,{modelValue:o(i).keywords,"onUpdate:modelValue":t[1]||(t[1]=l=>o(i).keywords=l),placeholder:"姓名/手机号",clearable:"",style:{width:"200px"},onKeyup:Ae(b,["enter"])},null,8,["modelValue","onKeyup"])]),_:1}),e(p,{label:"状态",prop:"status"},{default:a(()=>[e(A,{modelValue:o(i).status,"onUpdate:modelValue":t[2]||(t[2]=l=>o(i).status=l),placeholder:"全部",clearable:"",style:{width:"200px"}},{default:a(()=>[e(E,{label:"启用",value:"1"}),e(E,{label:"禁用",value:"0"})]),_:1},8,["modelValue"])]),_:1}),e(p,null,{default:a(()=>[e(m,{type:"primary",onClick:b},{default:a(()=>[e(J),r("搜索")]),_:1}),e(m,{onClick:V},{default:a(()=>[e(we),r(" 重置")]),_:1})]),_:1})]),_:1},8,["model"])]),e(W,{shadow:"never"},{header:a(()=>[_("div",Nt,[_("div",null,[C((u(),g(m,{type:"success",onClick:t[3]||(t[3]=l=>G())},{default:a(()=>[e(ye),r("新增")]),_:1})),[[$,["sys:user:add"]]])])])]),default:a(()=>[C((u(),g(ke,{data:o(X),onSelectionChange:pe},{default:a(()=>[e(f,{type:"selection",width:"50",align:"center"}),e(f,{label:"头像",width:"180"},{default:a(l=>[l.row.avatarUrl&&l.row.avatarUrl.length>0?(u(),g(ee,{key:0,shape:"square",size:40,src:l.row.avatarUrl},null,8,["src"])):(u(),g(ee,{key:1,shape:"square",size:40},{default:a(()=>[r(oe(l.row.name.length>2?l.row.name.substring(0,2):l.row.name),1)]),_:2},1024))]),_:1}),e(f,{key:"phone",label:"手机号",align:"center",width:"180",prop:"phone"}),e(f,{label:"姓名",width:"120",align:"center",prop:"name"}),e(f,{label:"性别",width:"100",align:"center",prop:"gender"},{default:a(l=>[r(oe(ge.value(l.row.gender)),1)]),_:1}),e(f,{label:"部门",width:"120",align:"center",prop:"deptName"}),e(f,{label:"状态",width:"180",align:"center",prop:"status"},{default:a(l=>[e(xe,{modelValue:l.row.status,"onUpdate:modelValue":U=>l.row.status=U,"inactive-value":0,"active-value":1,onChange:U=>ue(l.row)},null,8,["modelValue","onUpdate:modelValue","onChange"])]),_:1}),e(f,{label:"创建时间",align:"center",prop:"createTime",width:"180"}),e(f,{label:"操作",fixed:"right",width:"220"},{default:a(l=>[C((u(),g(m,{type:"primary",size:"small",link:"",onClick:U=>me(l.row)},{default:a(()=>[e(Ve),r("重置密码")]),_:2},1032,["onClick"])),[[$,["sys:user:reset_pwd"]]]),C((u(),g(m,{type:"primary",link:"",size:"small",onClick:U=>G(l.row.id)},{default:a(()=>[e(Ee),r("编辑")]),_:2},1032,["onClick"])),[[$,["sys:user:edit"]]]),C((u(),g(m,{type:"primary",link:"",size:"small",onClick:U=>fe(l.row.id)},{default:a(()=>[e(Ue),r("删除")]),_:2},1032,["onClick"])),[[$,["sys:user:delete"]]])]),_:1})]),_:1},8,["data"])),[[Te,o(h)]]),o(w)>0?(u(),g(Ce,{key:0,total:o(w),"onUpdate:total":t[4]||(t[4]=l=>P(w)?w.value=l:null),page:o(i).pageNum,"onUpdate:page":t[5]||(t[5]=l=>o(i).pageNum=l),limit:o(i).pageSize,"onUpdate:limit":t[6]||(t[6]=l=>o(i).pageSize=l),onPagination:b},null,8,["total","page","limit"])):Le("",!0)]),_:1})]),_:1})]),_:1}),e(ae,{modelValue:o(v).visible,"onUpdate:modelValue":t[14]||(t[14]=l=>o(v).visible=l),title:o(v).title,width:"600px","append-to-body":"",onClose:T},{footer:a(()=>[_("div",At,[e(m,{type:"primary",onClick:o(_e)},{default:a(()=>[r("确 定")]),_:1},8,["onClick"]),e(m,{onClick:T},{default:a(()=>[r("取 消")]),_:1})])]),default:a(()=>[e(L,{ref_key:"userFormRef",ref:D,model:o(s),rules:o(se),"label-width":"80px"},{default:a(()=>[e(p,{label:"头像",prop:"avatarUrl"},{default:a(()=>[e(Vt,{modelValue:o(s).avatarUrl,"onUpdate:modelValue":t[7]||(t[7]=l=>o(s).avatarUrl=l)},null,8,["modelValue"])]),_:1}),e(p,{label:"手机号",prop:"phone"},{default:a(()=>[e(z,{modelValue:o(s).phone,"onUpdate:modelValue":t[8]||(t[8]=l=>o(s).phone=l),placeholder:"请输入手机号"},null,8,["modelValue"])]),_:1}),e(p,{label:"姓名",prop:"name"},{default:a(()=>[e(z,{modelValue:o(s).name,"onUpdate:modelValue":t[9]||(t[9]=l=>o(s).name=l),placeholder:"请输入姓名"},null,8,["modelValue"])]),_:1}),e(p,{label:"所属部门",prop:"deptId"},{default:a(()=>[e(te,{modelValue:o(s).deptId,"onUpdate:modelValue":t[10]||(t[10]=l=>o(s).deptId=l),placeholder:"请选择所属部门",data:o(B),filterable:"",props:{label:"name",value:"id"},"check-strictly":"","render-after-expand":!1},null,8,["modelValue","data"])]),_:1}),e(p,{label:"性别",prop:"gender"},{default:a(()=>[e(A,{modelValue:o(s).gender,"onUpdate:modelValue":t[11]||(t[11]=l=>o(s).gender=l),placeholder:"请选择"},{default:a(()=>[e(E,{label:"男",value:1}),e(E,{label:"女",value:2})]),_:1},8,["modelValue"])]),_:1}),e(p,{label:"角色",prop:"roleIds"},{default:a(()=>[e(A,{modelValue:o(s).roleIds,"onUpdate:modelValue":t[12]||(t[12]=l=>o(s).roleIds=l),multiple:"",placeholder:"请选择"},{default:a(()=>[(u(!0),R(Pe,null,Oe(o(Z),l=>(u(),g(E,{key:l.id,label:l.name,value:l.id},null,8,["label","value"]))),128))]),_:1},8,["modelValue"])]),_:1}),e(p,{label:"状态",prop:"status"},{default:a(()=>[e(De,{modelValue:o(s).status,"onUpdate:modelValue":t[13]||(t[13]=l=>o(s).status=l)},{default:a(()=>[e(le,{label:1},{default:a(()=>[r("正常")]),_:1}),e(le,{label:0},{default:a(()=>[r("禁用")]),_:1})]),_:1},8,["modelValue"])]),_:1})]),_:1},8,["model","rules"])]),_:1},8,["modelValue","title"]),e(ae,{modelValue:o(F).visible,"onUpdate:modelValue":t[16]||(t[16]=l=>o(F).visible=l),title:o(F).title,width:"600px","append-to-body":"",onClose:N},{footer:a(()=>[_("div",Ot,[e(m,{type:"primary",onClick:he},{default:a(()=>[r("确 定")]),_:1}),e(m,{onClick:N},{default:a(()=>[r("取 消")]),_:1})])]),default:a(()=>[e(L,{"label-width":"80px"},{default:a(()=>[e(p,{label:"部门"},{default:a(()=>[e(te,{modelValue:o(y),"onUpdate:modelValue":t[15]||(t[15]=l=>P(y)?y.value=l:null),placeholder:"请选择部门",data:o(B),filterable:"","check-strictly":""},null,8,["modelValue","data"])]),_:1}),e(p,{label:"Excel"},{default:a(()=>[e(Fe,{class:"upload-demo",action:"",drag:"","auto-upload":!1,accept:"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/vnd.ms-excel","file-list":o(q),"on-change":ve,limit:1},{tip:a(()=>[Pt]),default:a(()=>[e(Be,{class:"el-icon--upload"},{default:a(()=>[e(Se)]),_:1}),Lt]),_:1},8,["file-list"])]),_:1})]),_:1})]),_:1},8,["modelValue","title"])])}}});export{Bl as default};
