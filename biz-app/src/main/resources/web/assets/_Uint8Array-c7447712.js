import{bP as l,bM as c,bQ as L,bO as _,bR as d,bS as K,bF as k,bT as R,bU as w,bV as y,bW as W,bX as u}from"./index-d5547550.js";var q=l(c,"WeakMap");const j=q;var N=9007199254740991;function z(t){return typeof t=="number"&&t>-1&&t%1==0&&t<=N}function X(t){return t!=null&&z(t.length)&&!L(t)}var H=Object.prototype;function Q(t){var r=t&&t.constructor,e=typeof r=="function"&&r.prototype||H;return t===e}function Y(t,r){for(var e=-1,n=Array(t);++e<t;)n[e]=r(e);return n}var Z="[object Arguments]";function $(t){return _(t)&&d(t)==Z}var V=Object.prototype,J=V.hasOwnProperty,tt=V.propertyIsEnumerable,et=$(function(){return arguments}())?$:function(t){return _(t)&&J.call(t,"callee")&&!tt.call(t,"callee")};const rt=et;function at(){return!1}var F=typeof exports=="object"&&exports&&!exports.nodeType&&exports,S=F&&typeof module=="object"&&module&&!module.nodeType&&module,nt=S&&S.exports===F,x=nt?c.Buffer:void 0,ot=x?x.isBuffer:void 0,st=ot||at;const it=st;var ct="[object Arguments]",ut="[object Array]",pt="[object Boolean]",ft="[object Date]",bt="[object Error]",gt="[object Function]",yt="[object Map]",lt="[object Number]",dt="[object Object]",ht="[object RegExp]",Tt="[object Set]",jt="[object String]",vt="[object WeakMap]",mt="[object ArrayBuffer]",At="[object DataView]",_t="[object Float32Array]",wt="[object Float64Array]",$t="[object Int8Array]",St="[object Int16Array]",xt="[object Int32Array]",Ot="[object Uint8Array]",Pt="[object Uint8ClampedArray]",It="[object Uint16Array]",Mt="[object Uint32Array]",a={};a[_t]=a[wt]=a[$t]=a[St]=a[xt]=a[Ot]=a[Pt]=a[It]=a[Mt]=!0;a[ct]=a[ut]=a[mt]=a[pt]=a[At]=a[ft]=a[bt]=a[gt]=a[yt]=a[lt]=a[dt]=a[ht]=a[Tt]=a[jt]=a[vt]=!1;function Et(t){return _(t)&&z(t.length)&&!!a[d(t)]}function Ct(t){return function(r){return t(r)}}var D=typeof exports=="object"&&exports&&!exports.nodeType&&exports,b=D&&typeof module=="object"&&module&&!module.nodeType&&module,Ut=b&&b.exports===D,T=Ut&&K.process,Bt=function(){try{var t=b&&b.require&&b.require("util").types;return t||T&&T.binding&&T.binding("util")}catch{}}();const O=Bt;var P=O&&O.isTypedArray,kt=P?Ct(P):Et;const zt=kt;var Vt=Object.prototype,Ft=Vt.hasOwnProperty;function Dt(t,r){var e=k(t),n=!e&&rt(t),s=!e&&!n&&it(t),p=!e&&!n&&!s&&zt(t),f=e||n||s||p,h=f?Y(t.length,String):[],G=h.length;for(var o in t)(r||Ft.call(t,o))&&!(f&&(o=="length"||s&&(o=="offset"||o=="parent")||p&&(o=="buffer"||o=="byteLength"||o=="byteOffset")||R(o,G)))&&h.push(o);return h}function Gt(t,r){return function(e){return t(r(e))}}var Lt=Gt(Object.keys,Object);const Kt=Lt;var Rt=Object.prototype,Wt=Rt.hasOwnProperty;function qt(t){if(!Q(t))return Kt(t);var r=[];for(var e in Object(t))Wt.call(t,e)&&e!="constructor"&&r.push(e);return r}function Nt(t){return X(t)?Dt(t):qt(t)}function Xt(t,r){for(var e=-1,n=r.length,s=t.length;++e<n;)t[s+e]=r[e];return t}function Ht(){this.__data__=new w,this.size=0}function Qt(t){var r=this.__data__,e=r.delete(t);return this.size=r.size,e}function Yt(t){return this.__data__.get(t)}function Zt(t){return this.__data__.has(t)}var Jt=200;function te(t,r){var e=this.__data__;if(e instanceof w){var n=e.__data__;if(!y||n.length<Jt-1)return n.push([t,r]),this.size=++e.size,this;e=this.__data__=new W(n)}return e.set(t,r),this.size=e.size,this}function g(t){var r=this.__data__=new w(t);this.size=r.size}g.prototype.clear=Ht;g.prototype.delete=Qt;g.prototype.get=Yt;g.prototype.has=Zt;g.prototype.set=te;function ee(t,r){for(var e=-1,n=t==null?0:t.length,s=0,p=[];++e<n;){var f=t[e];r(f,e,t)&&(p[s++]=f)}return p}function re(){return[]}var ae=Object.prototype,ne=ae.propertyIsEnumerable,I=Object.getOwnPropertySymbols,oe=I?function(t){return t==null?[]:(t=Object(t),ee(I(t),function(r){return ne.call(t,r)}))}:re;const se=oe;function ie(t,r,e){var n=r(t);return k(t)?n:Xt(n,e(t))}function je(t){return ie(t,Nt,se)}var ce=l(c,"DataView");const v=ce;var ue=l(c,"Promise");const m=ue;var pe=l(c,"Set");const A=pe;var M="[object Map]",fe="[object Object]",E="[object Promise]",C="[object Set]",U="[object WeakMap]",B="[object DataView]",be=u(v),ge=u(y),ye=u(m),le=u(A),de=u(j),i=d;(v&&i(new v(new ArrayBuffer(1)))!=B||y&&i(new y)!=M||m&&i(m.resolve())!=E||A&&i(new A)!=C||j&&i(new j)!=U)&&(i=function(t){var r=d(t),e=r==fe?t.constructor:void 0,n=e?u(e):"";if(n)switch(n){case be:return B;case ge:return M;case ye:return E;case le:return C;case de:return U}return r});const ve=i;var he=c.Uint8Array;const me=he;export{g as S,me as U,X as a,Dt as b,Xt as c,ie as d,ve as e,Ct as f,se as g,it as h,Q as i,je as j,Nt as k,zt as l,z as m,O as n,Gt as o,rt as p,A as q,re as s};
