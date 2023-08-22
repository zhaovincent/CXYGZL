import{d as J,h as P,r as v,a8 as W,o as X,a as s,c as _,k as l,a5 as n,w as a,a3 as m,ae as g,f as i,e3 as D,l as u,aY as C,am as b,R as N,a6 as S,q as h,ep as Z,eq as ee,er as te,es as oe,a7 as q,an as B,a0 as ne,aD as ae,aC as le}from"./index-d5547550.js";import{E as se}from"./el-tag-de6752b8.js";/* empty css                  */import{E as ce}from"./el-card-39a9c0d0.js";import"./el-tooltip-4ed993c7.js";import{E as ie}from"./el-popper-00977718.js";import{a as re,b as de,d as ue}from"./index-6d308f65.js";import{d as pe,e as fe,b as _e}from"./index-3b3587d2.js";import{E as me}from"./index-85bc974f.js";import{_ as he}from"./_plugin-vue_export-helper-c27b6911.js";const xe={class:"app-container"},ve={style:{width:"80%","margin-left":"10%","text-align":"right","margin-bottom":"20px"}},ge={class:"item additem",style:{height:"40px"}},ke={style:{height:"40px","line-height":"40px"}},ye={class:"last"},we={class:"card-header"},Ce={key:0,style:{"margin-left":"20px",width:"200px"}},be={key:1,style:{"margin-left":"20px",width:"200px"}},Be={style:{height:"60px","line-height":"60px"}},Ee={style:{"margin-left":"50px",height:"60px","line-height":"60px",width:"250px",overflow:"hidden","white-space":"nowrap","text-overflow":"ellipsis"}},Ge={style:{"margin-left":"50px",height:"60px","line-height":"60px",width:"300px",overflow:"hidden","white-space":"nowrap","text-overflow":"ellipsis"}},Ie={class:"last"},Te=J({__name:"group",setup($e){const{proxy:M}=P(),p=v(""),x=v(!1),y=v(0),E=v(),G=v([]);function R(){M.$isBlank(p.value)?x.value=!1:de({groupName:p.value}).then(()=>{q.success("新增成功"),x.value=!1,f()})}const w=W();function I(t){let o="/flow/create";t&&(o=o+"?groupId="+t),w.push(o)}function L(t){let o="/flow/create?flowId="+t.flowId;w.push(o)}function A(t){let o="/flow/create?cp=1&flowId="+t.flowId;w.push(o)}function z(t){B.confirm("确定要停用该流程吗?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(()=>{pe(t.flowId).then(o=>{f()})})}function K(t){B.confirm("确定要启用该流程吗?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(()=>{fe(t.flowId).then(o=>{f()})})}function O(t){B.confirm("确定要删除该流程吗?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(()=>{_e(t.flowId).then(o=>{f()})})}X(()=>{f()});function f(){re(!1).then(t=>{const{data:o}=t;G.value=o})}function Q(){x.value=!1,p.value="",y.value=new Date().getTime()}function U(){p.value="",y.value=new Date().getTime(),x.value=!0,ne(()=>{E.value.focus()})}function Y(t){ue(t).then(()=>{q.success("删除成功"),f()})}return(t,o)=>{const c=ae,j=le,r=ie,T=ce,H=me,$=se;return s(),_("div",xe,[l("div",ve,[n(c,{class:"button",onClick:U},{default:a(()=>[m("新建分组")]),_:1}),n(c,{class:"button",onClick:o[0]||(o[0]=g(d=>I(void 0),["stop"])),type:"primary",icon:i(D)},{default:a(()=>[m("创建流程")]),_:1},8,["icon"])]),x.value?(s(),u(T,{key:0,class:"box-card"},{default:a(()=>[l("div",ge,[l("div",ke,[(s(),u(j,{ref_key:"addGroupRef",ref:E,key:y.value,maxlength:"20",minlength:"2",modelValue:p.value,"onUpdate:modelValue":o[1]||(o[1]=d=>p.value=d),onBlur:g(R,["stop"]),placeholder:"分组名称",clearable:""},null,8,["modelValue","onBlur"]))]),l("div",ye,[n(r,{class:"box-item",effect:"dark",content:"删除",placement:"top"},{default:a(()=>[n(c,{text:"",icon:i(C),circle:"",onClick:g(Q,["stop"])},null,8,["icon","onClick"])]),_:1})])])]),_:1})):b("",!0),(s(!0),_(N,null,S(G.value,d=>(s(),u(T,{class:"box-card"},{header:a(()=>[l("div",we,[l("span",null,h(d.name),1),l("span",null,[n(r,{class:"box-item",effect:"dark",content:"创建流程",placement:"top"},{default:a(()=>[n(c,{onClick:g(e=>I(d.id),["stop"]),text:"",icon:i(D),circle:""},null,8,["onClick","icon"])]),_:2},1024),n(r,{class:"box-item",effect:"dark",content:"删除",placement:"top"},{default:a(()=>{var e;return[n(c,{text:"",icon:i(C),onClick:g(F=>Y(d.id),["stop"]),circle:"",disabled:((e=d.items)==null?void 0:e.length)>0},null,8,["icon","onClick","disabled"])]}),_:2},1024)])])]),default:a(()=>[(s(!0),_(N,null,S(d.items,(e,F)=>{var V;return s(),_("div",{key:F,class:"item"},[l("div",null,[n(H,{shape:"square",size:50,src:e.logo},null,8,["src"])]),((V=e.remark)==null?void 0:V.length)>0?(s(),_("div",Ce,[l("div",null,[m(h(e.name)+" ",1),e.stop?(s(),u($,{key:0,type:"danger"},{default:a(()=>[m("已停用")]),_:1})):b("",!0)]),l("div",null,h(e.remark),1)])):(s(),_("div",be,[l("div",Be,[m(h(e.name)+" ",1),e.stop?(s(),u($,{key:0,type:"danger"},{default:a(()=>[m("已停用")]),_:1})):b("",!0)])])),l("div",Ee,h(e.updated),1),l("div",Ge,h(e.rangeShow&&e.rangeShow.length>0?e.rangeShow:"所有人"),1),l("div",Ie,[n(r,{class:"box-item",effect:"dark",content:"编辑",placement:"top"},{default:a(()=>[n(c,{text:"",onClick:k=>L(e),icon:i(Z),circle:""},null,8,["onClick","icon"])]),_:2},1024),n(r,{class:"box-item",effect:"dark",content:"复制",placement:"top"},{default:a(()=>[n(c,{text:"",onClick:k=>A(e),icon:i(ee),circle:""},null,8,["onClick","icon"])]),_:2},1024),e.stop?(s(),u(r,{key:1,class:"box-item",effect:"dark",content:"启用",placement:"top"},{default:a(()=>[n(c,{onClick:k=>K(e),text:"",icon:i(oe),circle:""},null,8,["onClick","icon"])]),_:2},1024)):(s(),u(r,{key:0,class:"box-item",effect:"dark",content:"停用",placement:"top"},{default:a(()=>[n(c,{onClick:k=>z(e),text:"",icon:i(te),circle:""},null,8,["onClick","icon"])]),_:2},1024)),n(r,{class:"box-item",effect:"dark",content:"删除",placement:"top"},{default:a(()=>[n(c,{text:"",onClick:k=>O(e),icon:i(C),circle:""},null,8,["onClick","icon"])]),_:2},1024)])])}),128))]),_:2},1024))),256))])}}});const ze=he(Te,[["__scopeId","data-v-45797ccb"]]);export{ze as default};