import{D as b,r as M,h as fe,L as _e,a as u,l as g,w as r,a3 as p,q as R,f as l,a5 as o,cm as X,am as y,c as _,R as v,a6 as x,as as A,k as m,a0 as Y,E as ye,aC as ve}from"./index-d5547550.js";import{E as ce}from"./el-drawer-75c80c60.js";import{E as ge,a as Ve}from"./el-tab-pane-a6cb13f9.js";/* empty css                    *//* empty css                */import{a as be,E as Ee}from"./el-form-item-2de9c3c8.js";import{E as Ue}from"./el-input-number-f4eeab94.js";import"./el-tag-de6752b8.js";import{E as ke,a as Te}from"./el-select-46753b34.js";import"./el-scrollbar-cbe0c54f.js";import"./el-popper-00977718.js";/* empty css                       */import{a as he,E as Le}from"./el-row-a594fbeb.js";import{E as Ne,a as xe}from"./el-radio-92787871.js";import{E as ze}from"./el-text-a7cd3acd.js";/* empty css                */import{$ as we}from"./index-a7c7b111.js";import{p as Ie,s as Ce}from"./const-a11e1fef.js";import{u as De}from"./index-71f0fd6b.js";import{u as Se}from"./flow-165b4256.js";import Re from"./formPerm-3f783d49.js";import{_ as q}from"./selectAndShow.vue_vue_type_script_setup_true_lang-9eafc7ba.js";import{E as Oe}from"./index-222e6800.js";import"./use-dialog-27a1e524.js";import"./strings-fbdcd853.js";import"./_baseClone-e7eb389f.js";import"./_Uint8Array-c7447712.js";import"./index-da53d962.js";import"./isEqual-fe47b28a.js";import"./debounce-e2bc871f.js";import"./index-e8e7f773.js";import"./_plugin-vue_export-helper-c27b6911.js";import"./employeesDialog-54936475.js";/* empty css                  */import"./selectBox-26a3a753.js";/* empty css                  */import"./selectBox.vue_vue_type_style_index_0_scoped_82c14c6c_lang-f1c14f73.js";import"./index-85bc974f.js";import"./selectResult-4abbf083.js";/* empty css                                                                     *//* empty css                                                                        */import"./index-a6355ec2.js";import"./refs-5fd04da2.js";import"./orgItem-a67c9471.js";import"./flatten-36990bb8.js";import"./_baseFlatten-c80c9ba3.js";import"./_overRest-d3d6b67b.js";const Be=m("h4",null,"选择角色",-1),Fe=m("h4",null,"选择成员",-1),Pe=m("h4",null,"人员控件",-1),$e=m("h4",null,"部门控件",-1),Me=m("h4",null,"审批终点",-1),Ae=m("span",{style:{"font-size":"14px","margin-right":"5px"}},"到第",-1),qe=m("span",{style:{"font-size":"14px","margin-left":"5px"}},"级部门主管终止",-1),Ge=m("h4",null,"选择方式",-1),He=m("h4",null,"多人审批时采用的审批方式",-1),Je={style:{display:"block",width:"100%"}},Ke={style:{display:"block",width:"100%"}},Qe={style:{display:"block",width:"100%"}},We=m("h4",null,"指定审批层级",-1),Xe=m("span",{style:{"font-size":"14px","margin-right":"5px"}},"第",-1),Ye=m("span",{style:{"font-size":"14px","margin-left":"5px"}},"级部门主管",-1),Ze=m("h4",null,"审批人与发起人是同一人时",-1),je=m("h4",null,"审批人为空时",-1),el=m("h4",null,"审批被拒绝",-1),Wl={__name:"approverDrawer",setup(ll){var Z=b(()=>{let n=[];const t=[0,1];var i={},d={};z(void 0,G.step3,i,d,!0);var c=i[e.value.id];if(c==null)return[];for(var s of c){var V=d[s].type;t.indexOf(V)>-1&&d[s].id!=e.value.id&&n.push({id:d[s].id,name:d[s].nodeName})}return n});function z(n,t,i,d,c){if(T.$isNode(t)){var s=t.id;if(d[s]=t,T.$isNotBlank(n)){var L=i[n];let S=d[n].type;if((S==5||S==8)&&!c)i[s]=[];else{var E=T.$deepCopy(L);E.push(s),i[s]=E}}else{var V=[];V.push(s),i[s]=V}var k=t.type,U=t.childNode;if(k===5||k===8||k===4){var N=t.conditionNodes;for(var F of N)z(s,F,i,d,!1);T.$isNode(U)&&z(s,U,i,d,!0)}else T.$isNode(U)&&z(s,U,i,d,!0)}}let G=Se();const w=M(!1),j=(n,t)=>{n.edit=!0,Y(()=>{var i;(i=document.getElementById("btnNameRef"+t))==null||i.focus()})};let ee=b(()=>Ie[e.value.type]);const H=M(),le=()=>{w.value=!0,Y(()=>{H.value.focus()})},te=()=>{w.value=!1,e.value.nodeName=e.value.nodeName||ee},ae=n=>{n.edit=!1,T.$isBlank(n.name)&&(n.name=n.defaultName)},O=b(()=>G.step2),J=b(()=>O.value.filter(n=>n.type==="SelectUser"||n.type==="SelectMultiUser")),K=b(()=>O.value.filter(n=>n.type==="SelectDept"||n.type==="SelectMultiDept")),oe=()=>{let n=O.value;var t={};let i=e.value.formPerms;for(var d of n)if(t[d.id]="R",d.type==="Layout"&&(t[d.id]="E"),i[d.id]&&(t[d.id]=i[d.id]),d.type==="Layout"){let s=d.props.value;for(var c of s)t[c.id]="R",i[c.id]&&(t[c.id]=i[c.id])}e.value.formPerms=t},{proxy:T}=fe();let e=M({}),B=De(),{setApproverConfig:re,setApprover:se}=B,Q=b(()=>B.approverConfigData),ne=b(()=>B.approverDrawer),I=b({get(){return ne.value},set(){W()}});_e(Q,n=>{e.value=n.value});var C=b({get(){return e.value.formUserId},set(n){e.value.formUserName=J.value.filter(t=>t.id===n)[0].name,e.value.formUserId=n}}),D=b({get(){return e.value.formUserId},set(n){e.value.formUserId=n,e.value.formUserName=K.value.filter(t=>t.id===n)[0].name}});const ue=n=>{e.value.nodeUserList=[],e.value.formUserId="",e.value.formUserName=""},de=()=>{e.value.error=!we.checkApproval(e.value),re({value:e.value,flag:!0,id:Q.value.id}),W()},W=()=>{se(!1)};return(n,t)=>{const i=ye,d=ze,c=ve,s=Ne,V=he,L=Le,E=xe,k=ke,U=Te,N=Ue,F=be,S=Ee,P=ge,ie=Oe,pe=Ve,me=ce;return u(),g(me,{"append-to-body":!0,modelValue:l(I),"onUpdate:modelValue":t[16]||(t[16]=f=>A(I)?I.value=f:I=f),"show-close":!1,size:550,onOpen:oe,"before-close":de},{header:r(({close:f,titleId:h,titleClass:a})=>[w.value?y("",!0):(u(),g(d,{key:0,style:{cursor:"pointer"},tag:"b",size:"large",onClick:le},{default:r(()=>[p(R(l(e).nodeName)+" ",1),o(i,null,{default:r(()=>[o(l(X))]),_:1})]),_:1})),w.value?(u(),g(c,{key:1,ref_key:"titleInputRef",ref:H,onBlur:te,maxlength:"10",modelValue:l(e).nodeName,"onUpdate:modelValue":t[0]||(t[0]=$=>l(e).nodeName=$)},null,8,["modelValue"])):y("",!0)]),default:r(()=>[o(pe,{type:"border-card"},{default:r(()=>[o(P,{label:"设置审批人"},{default:r(()=>{var f,h;return[o(E,{modelValue:l(e).assignedType,"onUpdate:modelValue":t[1]||(t[1]=a=>l(e).assignedType=a),onChange:ue,class:"ml-4"},{default:r(()=>[o(L,null,{default:r(()=>[(u(!0),_(v,null,x(l(Ce),({value:a,label:$})=>(u(),g(V,{span:8,key:a},{default:r(()=>[o(s,{label:a},{default:r(()=>[p(R($),1)]),_:2},1032,["label"])]),_:2},1024))),128))]),_:1})]),_:1},8,["modelValue"]),l(e).assignedType===3?(u(),_(v,{key:0},[Be,o(q,{orgList:l(e).nodeUserList,"onUpdate:orgList":t[2]||(t[2]=a=>l(e).nodeUserList=a),type:"role",multiple:!0},null,8,["orgList"])],64)):y("",!0),l(e).assignedType===1?(u(),_(v,{key:1},[Fe,o(q,{orgList:l(e).nodeUserList,"onUpdate:orgList":t[3]||(t[3]=a=>l(e).nodeUserList=a),type:"org",multiple:!0},null,8,["orgList"])],64)):y("",!0),l(e).assignedType===8?(u(),_(v,{key:2},[Pe,o(U,{modelValue:l(C),"onUpdate:modelValue":t[4]||(t[4]=a=>A(C)?C.value=a:C=a),clearable:"",class:"m-2",placeholder:"请选择审批表单",size:"large"},{default:r(()=>[(u(!0),_(v,null,x(J.value,a=>(u(),g(k,{key:a.id,label:a.name,value:a.id},null,8,["label","value"]))),128))]),_:1},8,["modelValue"])],64)):y("",!0),l(e).assignedType===9?(u(),_(v,{key:3},[$e,o(U,{modelValue:l(D),"onUpdate:modelValue":t[5]||(t[5]=a=>A(D)?D.value=a:D=a),clearable:"",class:"m-2",placeholder:"请选择审批表单",size:"large"},{default:r(()=>[(u(!0),_(v,null,x(K.value,a=>(u(),g(k,{key:a.id,label:a.name,value:a.id},null,8,["label","value"]))),128))]),_:1},8,["modelValue"])],64)):y("",!0),l(e).assignedType===7?(u(),_(v,{key:4},[Me,Ae,o(N,{modelValue:l(e).deptLeaderLevel,"onUpdate:modelValue":t[6]||(t[6]=a=>l(e).deptLeaderLevel=a),step:1,min:1,max:20,"step-strictly":"",size:"small"},null,8,["modelValue"]),qe],64)):y("",!0),l(e).assignedType===4?(u(),_(v,{key:5},[Ge,o(E,{modelValue:l(e).multiple,"onUpdate:modelValue":t[7]||(t[7]=a=>l(e).multiple=a),class:"ml-4"},{default:r(()=>[o(s,{label:!1,size:"large"},{default:r(()=>[p("单选")]),_:1}),o(s,{label:!0,size:"large"},{default:r(()=>[p("多选")]),_:1})]),_:1},8,["modelValue"])],64)):y("",!0),(l(e).multiple===!0&&l(e).assignedType===4||l(e).assignedType===1||l(e).assignedType===9||l(e).assignedType===3||l(e).assignedType===7&&l(e).deptLeaderLevel>1||l(e).assignedType===8)&&l(e).assignedType!=5&&l(e).assignedType!=2?(u(),_(v,{key:6},[He,o(E,{modelValue:l(e).multipleMode,"onUpdate:modelValue":t[8]||(t[8]=a=>l(e).multipleMode=a),class:"ml-4"},{default:r(()=>[m("p",Je,[o(s,{label:1,size:"large"},{default:r(()=>[p("会签(默认需要所有审批人同意)")]),_:1})]),m("p",Ke,[o(s,{label:2,size:"large"},{default:r(()=>[p("或签(一名审批人同意即可)")]),_:1})]),m("p",Qe,[o(s,{label:3,size:"large"},{default:r(()=>[p("依次审批(按顺序依次审批)")]),_:1})])]),_:1},8,["modelValue"]),l(e).multipleMode==1?(u(),g(S,{key:0,"label-width":"180px"},{default:r(()=>[o(F,{label:"会签审核通过比例(%)"},{default:r(()=>[o(N,{modelValue:l(e).completeRate,"onUpdate:modelValue":t[9]||(t[9]=a=>l(e).completeRate=a),min:1,precision:2,"value-on-clear":"max","controls-position":"right",max:100},null,8,["modelValue"])]),_:1})]),_:1})):y("",!0)],64)):y("",!0),l(e).assignedType===2?(u(),_(v,{key:7},[We,Xe,o(N,{modelValue:l(e).deptLeaderLevel,"onUpdate:modelValue":t[10]||(t[10]=a=>l(e).deptLeaderLevel=a),step:1,min:1,max:20,"step-strictly":"",size:"small"},null,8,["modelValue"]),Ye],64)):y("",!0),(f=l(e).sameAsStarter)!=null&&f.handler?(u(),_(v,{key:8},[Ze,o(E,{modelValue:l(e).sameAsStarter.handler,"onUpdate:modelValue":t[11]||(t[11]=a=>l(e).sameAsStarter.handler=a)},{default:r(()=>[o(s,{style:{width:"30%"},label:"TO_CONTINUE",size:"large"},{default:r(()=>[p("继续处理")]),_:1}),o(s,{style:{width:"30%"},label:"TO_PASS",size:"large"},{default:r(()=>[p("自动通过")]),_:1}),o(s,{style:{width:"30%"},label:"TO_DEPT_LEADER",size:"large"},{default:r(()=>[p("转交给部门负责人处理")]),_:1}),o(s,{style:{width:"30%"},label:"TO_ADMIN",size:"large"},{default:r(()=>[p("转交给管理员处理")]),_:1})]),_:1},8,["modelValue"])],64)):y("",!0),je,o(E,{modelValue:l(e).nobody.handler,"onUpdate:modelValue":t[12]||(t[12]=a=>l(e).nobody.handler=a),class:"ml-4"},{default:r(()=>[o(s,{label:"TO_PASS",size:"large"},{default:r(()=>[p("自动通过")]),_:1}),o(s,{label:"TO_REFUSE",size:"large"},{default:r(()=>[p("自动拒绝")]),_:1}),o(s,{label:"TO_ADMIN",size:"large"},{default:r(()=>[p("转交给管理员")]),_:1}),o(s,{label:"TO_USER",size:"large"},{default:r(()=>[p("指定人员")]),_:1})]),_:1},8,["modelValue"]),l(e).nobody.handler==="TO_USER"?(u(),g(q,{key:9,orgList:l(e).nobody.assignedUser,"onUpdate:orgList":t[13]||(t[13]=a=>l(e).nobody.assignedUser=a),type:"user",multiple:!1},null,8,["orgList"])):y("",!0),(h=l(e).refuse)!=null&&h.handler?(u(),_(v,{key:10},[el,o(E,{modelValue:l(e).refuse.handler,"onUpdate:modelValue":t[14]||(t[14]=a=>l(e).refuse.handler=a),class:"ml-4"},{default:r(()=>[o(s,{label:"TO_END",size:"large"},{default:r(()=>[p("直接结束流程")]),_:1}),o(s,{label:"TO_NODE",size:"large"},{default:r(()=>[p("驳回到指定节点")]),_:1})]),_:1},8,["modelValue"]),l(e).refuse.handler==="TO_NODE"?(u(),g(U,{key:0,modelValue:l(e).refuse.nodeId,"onUpdate:modelValue":t[15]||(t[15]=a=>l(e).refuse.nodeId=a),placeholder:"驳回节点",style:{width:"100%","margin-bottom":"20px"}},{default:r(()=>[(u(!0),_(v,null,x(l(Z),a=>(u(),g(k,{key:a.id,label:a.name,value:a.id},null,8,["label","value"]))),128))]),_:1},8,["modelValue"])):y("",!0)],64)):y("",!0)]}),_:1}),o(P,{label:"操作权限"},{default:r(()=>[m("ul",null,[m("li",null,[o(L,null,{default:r(()=>[o(V,{span:12},{default:r(()=>[o(d,{tag:"b"},{default:r(()=>[p("权限名字")]),_:1})]),_:1}),o(V,{span:12},{default:r(()=>[o(d,{tag:"b"},{default:r(()=>[p("按钮名字")]),_:1})]),_:1})]),_:1})]),(u(!0),_(v,null,x(l(e).operList,(f,h)=>(u(),_("li",null,[o(L,null,{default:r(()=>[o(V,{span:12},{default:r(()=>[o(ie,{modelValue:f.checked,"onUpdate:modelValue":a=>f.checked=a,size:"large"},{default:r(()=>[p(R(f.defaultName),1)]),_:2},1032,["modelValue","onUpdate:modelValue"])]),_:2},1024),o(V,{span:12},{default:r(()=>[f.edit?(u(),g(c,{key:1,id:"btnNameRef"+h,onBlur:a=>ae(f),modelValue:f.name,"onUpdate:modelValue":a=>f.name=a,placeholder:"请输入按钮名字"},null,8,["id","onBlur","modelValue","onUpdate:modelValue"])):(u(),g(d,{key:0,onClick:a=>j(f,h)},{default:r(()=>[p(R(f.name)+" ",1),o(i,null,{default:r(()=>[o(l(X))]),_:1})]),_:2},1032,["onClick"]))]),_:2},1024)]),_:2},1024)]))),256))])]),_:1}),o(P,{label:"表单权限"},{default:r(()=>[o(Re,{"form-perm":l(e).formPerms},null,8,["form-perm"])]),_:1})]),_:1})]),_:1},8,["modelValue"])}}};export{Wl as default};