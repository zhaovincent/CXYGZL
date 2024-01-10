import{b as R,dW as N,c$ as k,O as $,b4 as h,cO as w,b3 as W,r as S,j as H,D as v,aJ as J,d3 as Q,d as y,u as B,a as G,c as I,k as g,P as z,cy as C,f as e,as as P,n as b,e as _,a3 as F,q as D,ae as K,_ as V,a0 as T,au as X,aG as Y,d1 as Z,d2 as x,o as ee,p as ae,K as oe,t as le,L as se,d4 as ne,s as te,v as A}from"./index-e01dade3.js";const L=R({size:N,disabled:Boolean,label:{type:[String,Number,Boolean],default:""}}),re=R({...L,modelValue:{type:[String,Number,Boolean],default:""},name:{type:String,default:""},border:Boolean}),M={[k]:s=>$(s)||h(s)||w(s),[W]:s=>$(s)||h(s)||w(s)},O=Symbol("radioGroupKey"),U=(s,c)=>{const n=S(),o=H(O,void 0),d=v(()=>!!o),m=v({get(){return d.value?o.modelValue:s.modelValue},set(i){d.value?o.changeEvent(i):c&&c(k,i),n.value.checked=s.modelValue===s.label}}),r=J(v(()=>o==null?void 0:o.size)),u=Q(v(()=>o==null?void 0:o.disabled)),l=S(!1),p=v(()=>u.value||d.value&&m.value!==s.label?-1:0);return{radioRef:n,isGroup:d,radioGroup:o,focus:l,size:r,disabled:u,tabIndex:p,modelValue:m}},ie=["value","name","disabled"],de=y({name:"ElRadio"}),ue=y({...de,props:re,emits:M,setup(s,{emit:c}){const n=s,o=B("radio"),{radioRef:d,radioGroup:m,focus:r,size:u,disabled:l,modelValue:p}=U(n,c);function i(){T(()=>c("change",p.value))}return(a,t)=>{var f;return G(),I("label",{class:b([e(o).b(),e(o).is("disabled",e(l)),e(o).is("focus",e(r)),e(o).is("bordered",a.border),e(o).is("checked",e(p)===a.label),e(o).m(e(u))])},[g("span",{class:b([e(o).e("input"),e(o).is("disabled",e(l)),e(o).is("checked",e(p)===a.label)])},[z(g("input",{ref_key:"radioRef",ref:d,"onUpdate:modelValue":t[0]||(t[0]=E=>P(p)?p.value=E:null),class:b(e(o).e("original")),value:a.label,name:a.name||((f=e(m))==null?void 0:f.name),disabled:e(l),type:"radio",onFocus:t[1]||(t[1]=E=>r.value=!0),onBlur:t[2]||(t[2]=E=>r.value=!1),onChange:i},null,42,ie),[[C,e(p)]]),g("span",{class:b(e(o).e("inner"))},null,2)],2),g("span",{class:b(e(o).e("label")),onKeydown:t[3]||(t[3]=K(()=>{},["stop"]))},[_(a.$slots,"default",{},()=>[F(D(a.label),1)])],34)],2)}}});var pe=V(ue,[["__file","/home/runner/work/element-plus/element-plus/packages/components/radio/src/radio.vue"]]);const ce=R({...L,name:{type:String,default:""}}),me=["value","name","disabled"],be=y({name:"ElRadioButton"}),fe=y({...be,props:ce,setup(s){const c=s,n=B("radio"),{radioRef:o,focus:d,size:m,disabled:r,modelValue:u,radioGroup:l}=U(c),p=v(()=>({backgroundColor:(l==null?void 0:l.fill)||"",borderColor:(l==null?void 0:l.fill)||"",boxShadow:l!=null&&l.fill?`-1px 0 0 0 ${l.fill}`:"",color:(l==null?void 0:l.textColor)||""}));return(i,a)=>{var t;return G(),I("label",{class:b([e(n).b("button"),e(n).is("active",e(u)===i.label),e(n).is("disabled",e(r)),e(n).is("focus",e(d)),e(n).bm("button",e(m))])},[z(g("input",{ref_key:"radioRef",ref:o,"onUpdate:modelValue":a[0]||(a[0]=f=>P(u)?u.value=f:null),class:b(e(n).be("button","original-radio")),value:i.label,type:"radio",name:i.name||((t=e(l))==null?void 0:t.name),disabled:e(r),onFocus:a[1]||(a[1]=f=>d.value=!0),onBlur:a[2]||(a[2]=f=>d.value=!1)},null,42,me),[[C,e(u)]]),g("span",{class:b(e(n).be("button","inner")),style:X(e(u)===i.label?e(p):{}),onKeydown:a[3]||(a[3]=K(()=>{},["stop"]))},[_(i.$slots,"default",{},()=>[F(D(i.label),1)])],38)],2)}}});var j=V(fe,[["__file","/home/runner/work/element-plus/element-plus/packages/components/radio/src/radio-button.vue"]]);const ve=R({id:{type:String,default:void 0},size:N,disabled:Boolean,modelValue:{type:[String,Number,Boolean],default:""},fill:{type:String,default:""},label:{type:String,default:void 0},textColor:{type:String,default:""},name:{type:String,default:void 0},validateEvent:{type:Boolean,default:!0}}),ge=M,ye=["id","aria-label","aria-labelledby"],Re=y({name:"ElRadioGroup"}),Ee=y({...Re,props:ve,emits:ge,setup(s,{emit:c}){const n=s,o=B("radio"),d=Y(),m=S(),{formItem:r}=Z(),{inputId:u,isLabeledByFormItem:l}=x(n,{formItemContext:r}),p=a=>{c(k,a),T(()=>c("change",a))};ee(()=>{const a=m.value.querySelectorAll("[type=radio]"),t=a[0];!Array.from(a).some(f=>f.checked)&&t&&(t.tabIndex=0)});const i=v(()=>n.name||d.value);return ae(O,oe({...le(n),changeEvent:p,name:i})),se(()=>n.modelValue,()=>{n.validateEvent&&(r==null||r.validate("change").catch(a=>ne()))}),(a,t)=>(G(),I("div",{id:e(u),ref_key:"radioGroupRef",ref:m,class:b(e(o).b("group")),role:"radiogroup","aria-label":e(l)?void 0:a.label||"radio-group","aria-labelledby":e(l)?e(r).labelId:void 0},[_(a.$slots,"default")],10,ye))}});var q=V(Ee,[["__file","/home/runner/work/element-plus/element-plus/packages/components/radio/src/radio-group.vue"]]);const ke=te(pe,{RadioButton:j,RadioGroup:q}),Be=A(q);A(j);export{ke as E,Be as a};