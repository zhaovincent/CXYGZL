import{r as L,D as i,L as E,a as D,l as P,w as f,a5 as p,f as u,as as S}from"./index-e01dade3.js";import{E as U}from"./el-drawer-51597e24.js";import{E as V,a as k}from"./el-tab-pane-5b128a44.js";import{_ as x}from"./selectAndShow.vue_vue_type_script_setup_true_lang-a708354f.js";import{$ as F}from"./index-126d0406.js";import{u as R}from"./index-219f5c7e.js";import{u as B}from"./flow-2349aa77.js";import T from"./formPerm-7089267e.js";import"./el-dialog-1471a1af.js";import"./refs-dd65b3fd.js";import"./strings-6464b592.js";import"./employeesDialog-3166dde1.js";import"./selectBox-a8819872.js";import"./el-avatar-63a3488d.js";import"./el-checkbox-123bf6dd.js";import"./isEqual-47390e81.js";import"./_Uint8Array-aeb50f83.js";import"./flatten-178b99bc.js";import"./_baseFlatten-6e5398bc.js";import"./_overRest-a79e0547.js";/* empty css                */import"./index-8f0c79b2.js";import"./_plugin-vue_export-helper-c27b6911.js";import"./selectResult-ca10c7f8.js";import"./orgItem-da1d9713.js";import"./el-tag-929563f7.js";import"./el-radio-70657465.js";const ie={__name:"copyerDrawer",setup($){let r=L({}),_=B();const v=i(()=>_.step2);let n=R(),{setCopyerConfig:y,setCopyer:w}=n,b=i(()=>n.copyerDrawer),d=i(()=>n.copyerConfig1),m=i({get(){return b.value},set(){c()}});E(d,l=>{r.value=l.value});const g=()=>{let l=v.value;var e={};let t=r.value.formPerms;for(var o of l)if(e[o.id]="R",t[o.id]&&(e[o.id]=t[o.id]),o.type==="Layout"){let s=o.props.value;for(var a of s)e[a.id]="R",t[a.id]&&(e[a.id]=t[a.id])}r.value.formPerms=e},C=()=>{r.value.error=!F.copyerStr(r.value),y({value:r.value,flag:!0,id:d.value.id}),c()},c=()=>{w(!1)};return(l,e)=>{const t=V,o=k,a=U;return D(),P(a,{"append-to-body":!0,title:"抄送人设置",modelValue:u(m),"onUpdate:modelValue":e[1]||(e[1]=s=>S(m)?m.value=s:m=s),onOpen:g,class:"set_copyer","show-close":!1,size:550,"before-close":C},{default:f(()=>[p(o,{type:"border-card"},{default:f(()=>[p(t,{label:"设置抄送人"},{default:f(()=>[p(x,{orgList:u(r).nodeUserList,"onUpdate:orgList":e[0]||(e[0]=s=>u(r).nodeUserList=s),type:"org",multiple:!0},null,8,["orgList"])]),_:1}),p(t,{label:"表单权限"},{default:f(()=>[p(T,{"hide-key":["E"],"form-perm":u(r).formPerms},null,8,["form-perm"])]),_:1})]),_:1})]),_:1},8,["modelValue"])}}};export{ie as default};
