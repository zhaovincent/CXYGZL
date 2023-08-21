import{d as I,u as z,p as B,N as C,e as U,b as V,i as $,D as F,a as t,c as l,k as i,n as m,f as o,au as j,l as v,w as k,m as q,E as P,am as c,q as u,_ as A,s as M,v as O,h as R,a2 as K,a5 as S,R as f,a6 as T,b5 as G,d$ as H,ej as J,a3 as b,r as N,L as Q,o as W}from"./index-e01dade3.js";import{E as X,a as Y}from"./el-tab-pane-5b128a44.js";import{E as Z}from"./el-avatar-63a3488d.js";import{_ as ee}from"./selectAndShow.vue_vue_type_script_setup_true_lang-a708354f.js";import{f as te}from"./index-3a646217.js";const se=I({name:"ElTimeline",setup(r,{slots:p}){const s=z("timeline");return B("timeline",p),()=>C("ul",{class:[s.b()]},[U(p,"default")])}}),ae=V({timestamp:{type:String,default:""},hideTimestamp:{type:Boolean,default:!1},center:{type:Boolean,default:!1},placement:{type:String,values:["top","bottom"],default:"bottom"},type:{type:String,values:["primary","success","warning","danger","info"],default:""},color:{type:String,default:""},size:{type:String,values:["normal","large"],default:"normal"},icon:{type:$},hollow:{type:Boolean,default:!1}}),le=I({name:"ElTimelineItem"}),oe=I({...le,props:ae,setup(r){const p=r,s=z("timeline-item"),h=F(()=>[s.e("node"),s.em("node",p.size||""),s.em("node",p.type||""),s.is("hollow",p.hollow)]);return(a,_)=>(t(),l("li",{class:m([o(s).b(),{[o(s).e("center")]:a.center}])},[i("div",{class:m(o(s).e("tail"))},null,2),a.$slots.dot?c("v-if",!0):(t(),l("div",{key:0,class:m(o(h)),style:j({backgroundColor:a.color})},[a.icon?(t(),v(o(P),{key:0,class:m(o(s).e("icon"))},{default:k(()=>[(t(),v(q(a.icon)))]),_:1},8,["class"])):c("v-if",!0)],6)),a.$slots.dot?(t(),l("div",{key:1,class:m(o(s).e("dot"))},[U(a.$slots,"dot")],2)):c("v-if",!0),i("div",{class:m(o(s).e("wrapper"))},[!a.hideTimestamp&&a.placement==="top"?(t(),l("div",{key:0,class:m([o(s).e("timestamp"),o(s).is("top")])},u(a.timestamp),3)):c("v-if",!0),i("div",{class:m(o(s).e("content"))},[U(a.$slots,"default")],2),!a.hideTimestamp&&a.placement==="bottom"?(t(),l("div",{key:1,class:m([o(s).e("timestamp"),o(s).is("bottom")])},u(a.timestamp),3)):c("v-if",!0)],2)],2))}});var L=A(oe,[["__file","/home/runner/work/element-plus/element-plus/packages/components/timeline/src/timeline-item.vue"]]);const re=M(se,{TimelineItem:L}),ne=O(L);const ie={style:{padding:"10px"}},ce={key:0,style:{color:"red"}},de={key:1},ue={key:2,style:{display:"flex","flex-direction":"row","flex-wrap":"wrap"}},pe={class:"node-show"},me={style:{overflow:"hidden"}},fe={class:"d1"},he={style:{"font-size":"8px",overflow:"hidden","white-space":"nowrap","text-overflow":"ellipsis","text-align":"center"}},ye={style:{display:"flex","flex-direction":"row"}},ve={style:{width:"40px","text-align":"center"}},_e={style:{height:"40px","line-height":"40px","font-size":"10px"}},ge=i("div",{style:{height:"40px","line-height":"40px","font-size":"10px"}}," (添加了评论) ",-1),we={class:"box-card",style:{"margin-bottom":"10px",padding:"5px","background-color":"var(--el-fill-color-light)"}},be={style:{padding:"0px 5px"}},ke=I({__name:"FlowNodeFormat",props:{nodeUser:{type:Object,dafault:()=>{}},row:{type:Array,dafault:()=>[]},disableSelect:{type:Boolean,default:!1}},setup(r){return R(),(p,s)=>{const h=Z,a=K("flow-node-format",!0),_=X,x=Y,w=ne,D=re;return t(),l("div",ie,[S(D,{reverse:!1},{default:k(()=>[(t(!0),l(f,null,T(r.row,(e,d)=>(t(),v(w,{key:d,size:"large",color:e.status!=2?e.status==1?"pink":"green":"blue",icon:e.status==2?o(G):e.status==1?o(H):o(J)},{default:k(()=>{var g;return[e.selectUser&&(!r.nodeUser[e.id]||((g=r.nodeUser[e.id])==null?void 0:g.length)==0)?(t(),l("p",ce,[b(u(e.name)+" ",1),e.placeholder&&e.placeholder.length>0?(t(),l(f,{key:0},[b("["+u(e.placeholder)+"]",1)],64)):c("",!0)])):(t(),l("p",de,[b(u(e.name)+" ",1),e.placeholder&&e.placeholder.length>0?(t(),l(f,{key:0},[b("["+u(e.placeholder)+"]",1)],64)):c("",!0)])),e.userVoList&&e.userVoList.length>0?(t(),l("div",ue,[(t(!0),l(f,null,T(e.userVoList,(n,y)=>(t(),l("div",{class:"box-card",key:y,style:{"margin-bottom":"10px",border:"0px solid red",width:"40px","text-align":"center"}},[i("div",pe,[i("div",me,[i("div",fe,[i("div",null,[S(h,{shape:"square",size:30,src:n.avatar},null,8,["src"])]),i("div",he,u(n.name),1)])])])]))),128))])):c("",!0),(t(!0),l(f,null,T(e.userVoList,(n,y)=>{var E;return t(),l(f,{key:y},[((E=n.approveDesc)==null?void 0:E.length)>0?(t(),l(f,{key:0},[i("div",ye,[i("div",ve,[S(h,{shape:"square",size:30,src:n.avatar},null,8,["src"])]),i("div",_e,u(n.name),1),ge]),i("div",we,u(n.approveDesc),1)],64)):c("",!0)],64)}),128)),e.selectUser?(t(),v(ee,{key:3,disabled:r.disableSelect,orgList:r.nodeUser[e.id],"onUpdate:orgList":n=>r.nodeUser[e.id]=n,type:"user",multiple:e.multiple},null,8,["disabled","orgList","onUpdate:orgList","multiple"])):c("",!0),e.branch.length>0?(t(),v(x,{key:4,type:"border-card"},{default:k(()=>[(t(!0),l(f,null,T(e.branch,(n,y)=>(t(),v(_,{label:"分支"+(y+1),name:y+"",key:y},{default:k(()=>[n.placeholder&&n.placeholder.length>0?(t(),l(f,{key:0},[b("["+u(n.placeholder)+"]",1)],64)):c("",!0),i("div",be,[S(a,{"node-user":r.nodeUser,disableSelect:r.disableSelect,row:n.children},null,8,["node-user","disableSelect","row"])])]),_:2},1032,["label","name"]))),128))]),_:2},1024)):c("",!0)]}),_:2},1032,["color","icon"]))),128))]),_:1})])}}}),Ue=I({__name:"FlowNodeFormatData",props:{flowId:{type:String,default:""},disableSelect:{type:Boolean,default:!1},taskId:{type:String,default:""},processInstanceId:{type:String,default:""},formData:{type:Object,dafault:()=>{}},selectUserNodeId:{type:Array,dafault:()=>[]}},setup(r,{expose:p}){const s=r,h=N([]),a=e=>{var d={flowId:s.flowId,processInstanceId:s.processInstanceId,paramMap:e,taskId:s.taskId};te(d).then(g=>{h.value=g.data})};Q(()=>s.formData,e=>{setTimeout(function(){new Date().getTime()-_.value>500&&(_.value=new Date().getTime(),a(e))},600)});const _=N();W(()=>{_.value=new Date().getTime(),a({})});const x=()=>{for(var e of s.selectUserNodeId){var d=w.value[e];if(!(d&&d.length>0))return!1}return!0},w=N({});return p({validate:x,formatSelectNodeUser:()=>{var e={};for(var d of s.selectUserNodeId){var g=w.value[d];e[d+"_assignee_select"]=g}return e}}),(e,d)=>(t(),v(ke,{row:h.value,"node-user":w.value,disableSelect:r.disableSelect,ref:"flowNodeFormatRef"},null,8,["row","node-user","disableSelect"]))}});export{Ue as _};
