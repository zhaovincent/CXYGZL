import{a$ as de,b0 as ve,b1 as fe,b2 as _e,b as Y,d as P,u as Z,D as y,a as p,c as S,k as l,e as R,f as e,l as T,n as c,m as ie,am as $,q as O,_ as ee,s as ae,b3 as re,b4 as le,h as te,L as X,p as Se,i as he,r as _,j as ge,o as ne,M as ye,K as ke,au as se,w as v,E as W,a5 as m,b5 as we,b6 as be,a3 as D,v as $e,aj as Ie,ab as Ce,a8 as Ee,P as U,Q,R as Ne,a6 as Be,b7 as Re,aB as Te,ag as xe,ah as De}from"./index-e01dade3.js";import{v as Oe}from"./el-loading-b076b1a7.js";import{E as Pe}from"./el-dialog-1471a1af.js";/* empty css                */import{E as Ve}from"./Money-53354be7.js";import{g as Fe,a as Je}from"./index-a2589c62.js";import je from"./step1-c7946133.js";import ze from"./step2-d0e4d13d.js";import Me from"./step3-9eb8f3ce.js";import{u as Le}from"./flow-2349aa77.js";import{u as qe}from"./el-tab-pane-5b128a44.js";import{_ as Ue}from"./_plugin-vue_export-helper-c27b6911.js";import"./refs-dd65b3fd.js";import"./el-input-number-182b17d2.js";import"./index-f7c59abe.js";import"./el-card-16bdf0d8.js";/* empty css                */import"./el-tag-929563f7.js";import"./el-select-c154265f.js";import"./el-popper-ee5283d6.js";import"./el-scrollbar-f2029ad1.js";import"./strings-6464b592.js";import"./isEqual-47390e81.js";import"./_Uint8Array-aeb50f83.js";import"./debounce-bf37b7f7.js";import"./index-6b517de5.js";import"./el-form-item-85be6a3a.js";import"./_baseClone-59dac119.js";import"./index-cffc2a4a.js";import"./SingleUpload-dfe85bb8.js";import"./el-progress-be452bc9.js";import"./plus-95522b44.js";import"./index-f7c9196e.js";import"./selectAndShow.vue_vue_type_script_setup_true_lang-a708354f.js";import"./employeesDialog-3166dde1.js";import"./selectBox-a8819872.js";import"./el-avatar-63a3488d.js";import"./el-checkbox-123bf6dd.js";import"./flatten-178b99bc.js";import"./_baseFlatten-6e5398bc.js";import"./_overRest-a79e0547.js";import"./index-8f0c79b2.js";import"./index-126d0406.js";import"./selectResult-ca10c7f8.js";import"./orgItem-da1d9713.js";import"./getFormWidget-4084fd80.js";import"./Date-e3a674b4.js";import"./el-date-picker-b8fb5128.js";import"./panel-time-pick-cbec15ad.js";import"./DateTime-e4a39557.js";import"./Description-c9397ba1.js";import"./Empty-b60a7a2f.js";import"./Input-741e75d5.js";import"./MultiSelect-f62683ec.js";import"./Number-4a71bbb3.js";import"./SelectDept-e55ebbaf.js";import"./SelectMultiDept-865ded1b.js";import"./SelectMultiUser-f2df4170.js";import"./SelectUser-350b1f1c.js";import"./SingleSelect-4f2549a6.js";import"./Textarea-33dea2bf.js";import"./Time-5f63d134.js";import"./el-time-picker-1aa4e9cb.js";import"./isArrayLikeObject-a5301880.js";import"./UploadFile-9daeda9f.js";import"./UploadImage-8c59ffb5.js";import"./MultiUpload-c72eb43c.js";import"./Date-8a1ce335.js";import"./DateTime-7bdf6b3b.js";import"./Description-c34ca8b8.js";import"./Input-75d2b6d1.js";import"./Money-ac057bdd.js";import"./MultiSelect-1d32af34.js";import"./el-link-b55a22bf.js";import"./Number-87253cb5.js";import"./SelectDept-c14d1ff2.js";import"./SelectMultiDept-bdeac07e.js";import"./SelectMultiUser-cd45c19e.js";import"./SelectUser-a7c9324b.js";import"./SingleSelect-2f82066a.js";import"./Textarea-c17d237d.js";import"./Time-73b7a899.js";import"./UploadFile-9a1c4093.js";import"./UploadImage-ece33454.js";import"./nodeWrap-cd57747a.js";import"./addNode-6db4a717.js";import"./el-popover-70136418.js";import"./dropdown-b5481c97.js";import"./const-36faa2f4.js";/* empty css                                                                 */import"./index-219f5c7e.js";import"./promoterDrawer-5de23e1f.js";import"./el-drawer-51597e24.js";import"./formPerm-7089267e.js";import"./el-radio-70657465.js";import"./approverDrawer-1bdbe5bd.js";import"./el-divider-84ed2554.js";import"./el-row-e9611a44.js";import"./copyerDrawer-e587977b.js";import"./conditionDrawer-a2c3cd52.js";import"./el-switch-e9147eb4.js";import"./condition-59a2bdde.js";const J={success:"icon-success",warning:"icon-warning",error:"icon-error",info:"icon-info"},oe={[J.success]:de,[J.warning]:ve,[J.error]:fe,[J.info]:_e},We=Y({title:{type:String,default:""},subTitle:{type:String,default:""},icon:{type:String,values:["success","warning","info","error"],default:"info"}}),Ge=P({name:"ElResult"}),Ke=P({...Ge,props:We,setup(h){const g=h,t=Z("result"),u=y(()=>{const i=g.icon,d=i&&J[i]?J[i]:"icon-info",o=oe[d]||oe["icon-info"];return{class:d,component:o}});return(i,d)=>(p(),S("div",{class:c(e(t).b())},[l("div",{class:c(e(t).e("icon"))},[R(i.$slots,"icon",{},()=>[e(u).component?(p(),T(ie(e(u).component),{key:0,class:c(e(u).class)},null,8,["class"])):$("v-if",!0)])],2),i.title||i.$slots.title?(p(),S("div",{key:0,class:c(e(t).e("title"))},[R(i.$slots,"title",{},()=>[l("p",null,O(i.title),1)])],2)):$("v-if",!0),i.subTitle||i.$slots["sub-title"]?(p(),S("div",{key:1,class:c(e(t).e("subtitle"))},[R(i.$slots,"sub-title",{},()=>[l("p",null,O(i.subTitle),1)])],2)):$("v-if",!0),i.$slots.extra?(p(),S("div",{key:2,class:c(e(t).e("extra"))},[R(i.$slots,"extra")],2)):$("v-if",!0)],2))}});var Ae=ee(Ke,[["__file","/home/runner/work/element-plus/element-plus/packages/components/result/src/result.vue"]]);const He=ae(Ae),Qe=Y({space:{type:[Number,String],default:""},active:{type:Number,default:0},direction:{type:String,default:"horizontal",values:["horizontal","vertical"]},alignCenter:{type:Boolean},simple:{type:Boolean},finishStatus:{type:String,values:["wait","process","finish","error","success"],default:"finish"},processStatus:{type:String,values:["wait","process","finish","error","success"],default:"process"}}),Xe={[re]:(h,g)=>[h,g].every(le)},Ye=P({name:"ElSteps"}),Ze=P({...Ye,props:Qe,emits:Xe,setup(h,{emit:g}){const t=h,u=Z("steps"),{children:i,addChild:d,removeChild:o}=qe(te(),"ElStep");return X(i,()=>{i.value.forEach((f,a)=>{f.setIndex(a)})}),Se("ElSteps",{props:t,steps:i,addStep:d,removeStep:o}),X(()=>t.active,(f,a)=>{g(re,f,a)}),(f,a)=>(p(),S("div",{class:c([e(u).b(),e(u).m(f.simple?"simple":f.direction)])},[R(f.$slots,"default")],2))}});var et=ee(Ze,[["__file","/home/runner/work/element-plus/element-plus/packages/components/steps/src/steps.vue"]]);const tt=Y({title:{type:String,default:""},icon:{type:he},description:{type:String,default:""},status:{type:String,values:["","wait","process","finish","error","success"],default:""}}),st=P({name:"ElStep"}),ot=P({...st,props:tt,setup(h){const g=h,t=Z("step"),u=_(-1),i=_({}),d=_(""),o=ge("ElSteps"),f=te();ne(()=>{X([()=>o.props.active,()=>o.props.processStatus,()=>o.props.finishStatus],([s])=>{H(s)},{immediate:!0})}),ye(()=>{o.removeStep(q.uid)});const a=y(()=>g.status||d.value),I=y(()=>{const s=o.steps.value[u.value-1];return s?s.currentStatus:"wait"}),C=y(()=>o.props.alignCenter),N=y(()=>o.props.direction==="vertical"),E=y(()=>o.props.simple),j=y(()=>o.steps.value.length),V=y(()=>{var s;return((s=o.steps.value[j.value-1])==null?void 0:s.uid)===(f==null?void 0:f.uid)}),x=y(()=>E.value?"":o.props.space),M=y(()=>[t.b(),t.is(E.value?"simple":o.props.direction),t.is("flex",V.value&&!x.value&&!C.value),t.is("center",C.value&&!N.value&&!E.value)]),L=y(()=>{const s={flexBasis:le(x.value)?`${x.value}px`:x.value?x.value:`${100/(j.value-(C.value?0:1))}%`};return N.value||V.value&&(s.maxWidth=`${100/j.value}%`),s}),K=s=>{u.value=s},A=s=>{const n=s==="wait",r={transitionDelay:`${n?"-":""}${150*u.value}ms`},k=s===o.props.processStatus||n?0:100;r.borderWidth=k&&!E.value?"1px":0,r[o.props.direction==="vertical"?"height":"width"]=`${k}%`,i.value=r},H=s=>{s>u.value?d.value=o.props.finishStatus:s===u.value&&I.value!=="error"?d.value=o.props.processStatus:d.value="wait";const n=o.steps.value[u.value-1];n&&n.calcProgress(d.value)},q=ke({uid:f.uid,currentStatus:a,setIndex:K,calcProgress:A});return o.addStep(q),(s,n)=>(p(),S("div",{style:se(e(L)),class:c(e(M))},[$(" icon & line "),l("div",{class:c([e(t).e("head"),e(t).is(e(a))])},[e(E)?$("v-if",!0):(p(),S("div",{key:0,class:c(e(t).e("line"))},[l("i",{class:c(e(t).e("line-inner")),style:se(i.value)},null,6)],2)),l("div",{class:c([e(t).e("icon"),e(t).is(s.icon||s.$slots.icon?"icon":"text")])},[R(s.$slots,"icon",{},()=>[s.icon?(p(),T(e(W),{key:0,class:c(e(t).e("icon-inner"))},{default:v(()=>[(p(),T(ie(s.icon)))]),_:1},8,["class"])):e(a)==="success"?(p(),T(e(W),{key:1,class:c([e(t).e("icon-inner"),e(t).is("status")])},{default:v(()=>[m(e(we))]),_:1},8,["class"])):e(a)==="error"?(p(),T(e(W),{key:2,class:c([e(t).e("icon-inner"),e(t).is("status")])},{default:v(()=>[m(e(be))]),_:1},8,["class"])):e(E)?$("v-if",!0):(p(),S("div",{key:3,class:c(e(t).e("icon-inner"))},O(u.value+1),3))])],2)],2),$(" title & description "),l("div",{class:c(e(t).e("main"))},[l("div",{class:c([e(t).e("title"),e(t).is(e(a))])},[R(s.$slots,"title",{},()=>[D(O(s.title),1)])],2),e(E)?(p(),S("div",{key:0,class:c(e(t).e("arrow"))},null,2)):(p(),S("div",{key:1,class:c([e(t).e("description"),e(t).is(e(a))])},[R(s.$slots,"description",{},()=>[D(O(s.description),1)])],2))],2)],6))}});var pe=ee(ot,[["__file","/home/runner/work/element-plus/element-plus/packages/components/steps/src/item.vue"]]);const it=ae(et,{Step:pe}),at=$e(pe);const G=h=>(xe("data-v-253dafda"),h=h(),De(),h),rt={class:"titlebar"},lt={class:"f1"},nt={class:"f2"},pt=["activeStep"],ct=["activeStep"],ut=G(()=>l("span",null,"基础信息",-1)),mt=["activeStep"],dt=["activeStep"],vt=G(()=>l("span",null,"表单设计",-1)),ft=["activeStep"],_t=["activeStep"],St=G(()=>l("span",null,"流程设计",-1)),ht={class:"f3"},gt=G(()=>l("div",{style:{height:"5px","background-color":"white","margin-bottom":"0px"}},null,-1)),yt={style:{"text-align":"center"}},kt={style:{display:"inline-block",border:"0px solid red",width:"100px",height:"100px"}},wt=P({__name:"all",setup(h){const{proxy:g}=te();let t=Le();const u=_(),i=_(),d=_(),o=_([]),f=y(()=>{let n=t.step1.name;return g.$isBlank(n)?"未命名表单":n}),a=_(0),I=_(0),C=_(!1),N=_(!1),E=()=>{a.value=I.value,C.value=!1},j=n=>{o.value=[],I.value=0,C.value=!0,N.value=!0,setTimeout(function(){K()},500)},V=Ie(),x=Ce();ne(()=>{const n=x.query,r=n.groupId??"",k=n.flowId??"",w=n.cp??"";if(g.$isNotBlank(r)&&(L.value=r),g.$isNotBlank(k))Fe(k).then(B=>{var{data:b}=B;t.step1.admin=JSON.parse(b.admin),t.step1.name=b.name,t.step1.logo=b.logo,(!w||parseInt(w)!==1)&&(t.step1.flowId=k),t.step1.remark=b.remark,t.step1.groupId=b.groupId,t.setStep2(JSON.parse(b.formItems)),M.value=JSON.parse(b.process)});else{let B=V.userId,b=V.nickname,z=V.avatar;t.step1.admin=[{id:B,name:b,avatar:z,type:"user"}]}});const M=_();var L=_();const K=()=>{u.value.validate(function(n,r){n?(I.value=1,setTimeout(function(){A()},500)):(N.value=!1,o.value=r)})},A=()=>{i.value.validate(function(n,r){n?setTimeout(function(){I.value=2,H()}):(N.value=!1,o.value=r)})},H=()=>{setTimeout(function(){d.value.validate(function(n,r){n?I.value=3:(N.value=!1,o.value=r)})})},q=Ee(),s=()=>{d.value.getProcessData().then(n=>{let r=t.step1,k=t.step2,w=g.$deepCopy(r);w.formItems=JSON.stringify(k),w.process=JSON.stringify(n),w.admin=JSON.stringify(r.admin),Je(w).then(B=>{C.value=!1,t.$reset(),q.push("/flow/group")})})};return(n,r)=>{const k=Ve,w=Te,B=at,b=it,z=He,ce=W,ue=Pe,me=Oe;return p(),S("div",null,[l("div",rt,[l("div",lt,[m(k,{tag:"b",size:"large",type:"primary"},{default:v(()=>[D(O(f.value),1)]),_:1})]),l("div",nt,[l("span",{class:"center_t",effect:"dark",activeStep:a.value==0,onClick:r[0]||(r[0]=F=>a.value=0)},[l("span",{activeStep:a.value==0},"1",8,ct),ut],8,pt),l("span",{class:"center_t",effect:"dark",activeStep:a.value==1,onClick:r[1]||(r[1]=F=>a.value=1)},[l("span",{activeStep:a.value==1},"2",8,dt),vt],8,mt),l("span",{class:"center_t",effect:"dark",activeStep:a.value==2,onClick:r[2]||(r[2]=F=>a.value=2)},[l("span",{activeStep:a.value==2},"3",8,_t),St],8,ft)]),l("div",ht,[m(w,{type:"primary",onClick:j},{default:v(()=>[D("发 布")]),_:1})])]),gt,U(m(je,{groupId:e(L),ref_key:"step1Ref",ref:u},null,8,["groupId"]),[[Q,a.value===0]]),U(m(ze,{ref_key:"step2Ref",ref:i},null,512),[[Q,a.value===1]]),U(m(Me,{nodeConfigObj:M.value,ref_key:"step3Ref",ref:d},null,8,["nodeConfigObj"]),[[Q,a.value===2]]),m(ue,{modelValue:C.value,"onUpdate:modelValue":r[3]||(r[3]=F=>C.value=F),title:"流程检查"},{default:v(()=>[m(b,{active:I.value,"finish-status":"success",simple:"",style:{"margin-top":"20px"}},{default:v(()=>[m(B,{title:"基础信息"}),m(B,{title:"表单设计"}),m(B,{title:"流程设计"})]),_:1},8,["active"]),l("div",yt,[I.value==3?(p(),T(z,{key:0,icon:"success",title:"检查成功","sub-title":"流程检查完成，现在提交？"},{extra:v(()=>[m(w,{type:"primary",onClick:s},{default:v(()=>[D("提交")]),_:1})]),_:1})):$("",!0),o.value.length==0&&C.value&&N.value&&I.value<3?(p(),T(z,{key:1,title:"检查中","sub-title":"正在检查流程信息"},{icon:v(()=>[U(l("span",kt,null,512),[[me,!0]])]),_:1})):$("",!0),o.value.length>0?(p(),T(z,{key:2,icon:"error",title:"检查失败"},{"sub-title":v(()=>[(p(!0),S(Ne,null,Be(o.value,F=>(p(),S("div",null,[m(k,{type:"danger"},{default:v(()=>[m(ce,null,{default:v(()=>[m(e(Re))]),_:1}),D(" "+O(F),1)]),_:2},1024)]))),256))]),extra:v(()=>[m(w,{type:"primary",onClick:E},{default:v(()=>[D("去修改")]),_:1})]),_:1})):$("",!0)])]),_:1},8,["modelValue"])])}}});const ho=Ue(wt,[["__scopeId","data-v-253dafda"]]);export{ho as default};