(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([[6],{"+5p2":function(module,exports,__webpack_require__){eval('module.exports = __webpack_require__.p + "static/arr2.c3ccefe5.png";\n\n//# sourceURL=webpack:///./src/assets/arr2.png?')},"/i1R":function(module,__webpack_exports__,__webpack_require__){"use strict";eval('__webpack_require__.r(__webpack_exports__);\n/* harmony import */ var react__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__("q1tI");\n/* harmony import */ var react__WEBPACK_IMPORTED_MODULE_0___default = /*#__PURE__*/__webpack_require__.n(react__WEBPACK_IMPORTED_MODULE_0__);\n/* harmony import */ var react_countup__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__("PHNs");\n/* harmony import */ var react_countup__WEBPACK_IMPORTED_MODULE_1___default = /*#__PURE__*/__webpack_require__.n(react_countup__WEBPACK_IMPORTED_MODULE_1__);\n/* harmony import */ var _logo__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__("J7IK");\n/* harmony import */ var _arrow__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__("y8iU");\n/* harmony import */ var _assets_page3_m_png__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__("gDDM");\n/* harmony import */ var _assets_page3_m_png__WEBPACK_IMPORTED_MODULE_4___default = /*#__PURE__*/__webpack_require__.n(_assets_page3_m_png__WEBPACK_IMPORTED_MODULE_4__);\n/* harmony import */ var _assets_page3_w_png__WEBPACK_IMPORTED_MODULE_5__ = __webpack_require__("gIJL");\n/* harmony import */ var _assets_page3_w_png__WEBPACK_IMPORTED_MODULE_5___default = /*#__PURE__*/__webpack_require__.n(_assets_page3_w_png__WEBPACK_IMPORTED_MODULE_5__);\n/* harmony import */ var _index_less__WEBPACK_IMPORTED_MODULE_6__ = __webpack_require__("CWlU");\n/* harmony import */ var _index_less__WEBPACK_IMPORTED_MODULE_6___default = /*#__PURE__*/__webpack_require__.n(_index_less__WEBPACK_IMPORTED_MODULE_6__);\n\n\n\n\n\n\n\n/* harmony default export */ __webpack_exports__["default"] = (function (_ref) {\n  var _ref$animate = _ref.animate,\n      animate = _ref$animate === void 0 ? false : _ref$animate,\n      _ref$sex = _ref.sex,\n      sex = _ref$sex === void 0 ? 1 : _ref$sex;\n  return /*#__PURE__*/react__WEBPACK_IMPORTED_MODULE_0___default.a.createElement("div", {\n    className: "page page3"\n  }, /*#__PURE__*/react__WEBPACK_IMPORTED_MODULE_0___default.a.createElement(_logo__WEBPACK_IMPORTED_MODULE_2__[/* default */ "a"], {\n    type: 2,\n    className: "page3-header",\n    animate: animate\n  }), /*#__PURE__*/react__WEBPACK_IMPORTED_MODULE_0___default.a.createElement("img", {\n    className: "person animate__animated ".concat(animate ? \'animate__zoomIn\' : \'\'),\n    src: sex = 1 ? _assets_page3_m_png__WEBPACK_IMPORTED_MODULE_4___default.a : undefined\n  }), /*#__PURE__*/react__WEBPACK_IMPORTED_MODULE_0___default.a.createElement("ul", {\n    className: "text animate__animated ".concat(animate ? \'animate__animated\' : \'\')\n  }, /*#__PURE__*/react__WEBPACK_IMPORTED_MODULE_0___default.a.createElement("li", null, "1\\u5E74\\u6765\\uFF0C\\u60A8\\u501F\\u4E86", /*#__PURE__*/react__WEBPACK_IMPORTED_MODULE_0___default.a.createElement("span", null, animate ? /*#__PURE__*/react__WEBPACK_IMPORTED_MODULE_0___default.a.createElement(react_countup__WEBPACK_IMPORTED_MODULE_1___default.a, {\n    end: 65\n  }) : null), "\\u672C\\u4E66", /*#__PURE__*/react__WEBPACK_IMPORTED_MODULE_0___default.a.createElement("span", null, "0"), "\\u672C\\u671F\\u520A"), /*#__PURE__*/react__WEBPACK_IMPORTED_MODULE_0___default.a.createElement("li", null, "\\u8BFB\\u5FC6\\u8D85\\u8FC7", /*#__PURE__*/react__WEBPACK_IMPORTED_MODULE_0___default.a.createElement("span", null, animate ? /*#__PURE__*/react__WEBPACK_IMPORTED_MODULE_0___default.a.createElement(react_countup__WEBPACK_IMPORTED_MODULE_1___default.a, {\n    end: 936\n  }) : null), "\\u4E07\\u5B57\\u5B66\\u5BCC", /*#__PURE__*/react__WEBPACK_IMPORTED_MODULE_0___default.a.createElement("span", null, animate ? /*#__PURE__*/react__WEBPACK_IMPORTED_MODULE_0___default.a.createElement(react_countup__WEBPACK_IMPORTED_MODULE_1___default.a, {\n    end: 5\n  }) : null), "\\u8F66\\uFF0C\\u81EA\\u7136\\u5E05\\u4E0D\\u53EF\\u6321"), /*#__PURE__*/react__WEBPACK_IMPORTED_MODULE_0___default.a.createElement("br", null), /*#__PURE__*/react__WEBPACK_IMPORTED_MODULE_0___default.a.createElement("li", null, "\\u5BF9\\u4E00\\u4E2A\\u7231\\u9605\\u8BFB\\u7684\\u4EBA"), /*#__PURE__*/react__WEBPACK_IMPORTED_MODULE_0___default.a.createElement("li", null, "\\u9AD8\\u5BCC\\u5E05\\u6709\\u4E86\\u65B0\\u7684\\u6DB5\\u4E49")), /*#__PURE__*/react__WEBPACK_IMPORTED_MODULE_0___default.a.createElement(_arrow__WEBPACK_IMPORTED_MODULE_3__[/* default */ "a"], {\n    arr: 2\n  }));\n});\n\n//# sourceURL=webpack:///./src/components/page3/index.jsx?')},"2W6z":function(module,exports,__webpack_require__){"use strict";eval("/**\n * Copyright (c) 2014-present, Facebook, Inc.\n *\n * This source code is licensed under the MIT license found in the\n * LICENSE file in the root directory of this source tree.\n */\n\n\n\n/**\n * Similar to invariant but only logs a warning if the condition is not met.\n * This can be used to log issues in development environments in critical\n * paths. Removing the logging code for production environments will keep the\n * same logic and follow the same code paths.\n */\n\nvar __DEV__ = \"production\" !== 'production';\n\nvar warning = function() {};\n\nif (__DEV__) {\n  var printWarning = function printWarning(format, args) {\n    var len = arguments.length;\n    args = new Array(len > 1 ? len - 1 : 0);\n    for (var key = 1; key < len; key++) {\n      args[key - 1] = arguments[key];\n    }\n    var argIndex = 0;\n    var message = 'Warning: ' +\n      format.replace(/%s/g, function() {\n        return args[argIndex++];\n      });\n    if (typeof console !== 'undefined') {\n      console.error(message);\n    }\n    try {\n      // --- Welcome to debugging React ---\n      // This error was thrown as a convenience so that you can use this stack\n      // to find the callsite that caused this warning to fire.\n      throw new Error(message);\n    } catch (x) {}\n  }\n\n  warning = function(condition, format, args) {\n    var len = arguments.length;\n    args = new Array(len > 2 ? len - 2 : 0);\n    for (var key = 2; key < len; key++) {\n      args[key - 2] = arguments[key];\n    }\n    if (format === undefined) {\n      throw new Error(\n          '`warning(condition, format, ...args)` requires a warning ' +\n          'message argument'\n      );\n    }\n    if (!condition) {\n      printWarning.apply(null, [format].concat(args));\n    }\n  };\n}\n\nmodule.exports = warning;\n\n\n//# sourceURL=webpack:///./node_modules/warning/warning.js?")},CWlU:function(module,exports,__webpack_require__){eval("// extracted by mini-css-extract-plugin\n\n//# sourceURL=webpack:///./src/components/page3/index.less?")},GiOn:function(module,exports,__webpack_require__){eval('var __WEBPACK_AMD_DEFINE_FACTORY__, __WEBPACK_AMD_DEFINE_RESULT__;!function(a,n){ true?!(__WEBPACK_AMD_DEFINE_FACTORY__ = (n),\n\t\t\t\t__WEBPACK_AMD_DEFINE_RESULT__ = (typeof __WEBPACK_AMD_DEFINE_FACTORY__ === \'function\' ?\n\t\t\t\t(__WEBPACK_AMD_DEFINE_FACTORY__.call(exports, __webpack_require__, exports, module)) :\n\t\t\t\t__WEBPACK_AMD_DEFINE_FACTORY__),\n\t\t\t\t__WEBPACK_AMD_DEFINE_RESULT__ !== undefined && (module.exports = __WEBPACK_AMD_DEFINE_RESULT__)):undefined}(this,function(a,n,t){var e=function(a,n,t,e,i,r){function o(a){var n,t,e,i,r,o,s=a<0;if(a=Math.abs(a).toFixed(l.decimals),a+="",n=a.split("."),t=n[0],e=n.length>1?l.options.decimal+n[1]:"",l.options.useGrouping){for(i="",r=0,o=t.length;r<o;++r)0!==r&&r%3===0&&(i=l.options.separator+i),i=t[o-r-1]+i;t=i}return l.options.numerals.length&&(t=t.replace(/[0-9]/g,function(a){return l.options.numerals[+a]}),e=e.replace(/[0-9]/g,function(a){return l.options.numerals[+a]})),(s?"-":"")+l.options.prefix+t+e+l.options.suffix}function s(a,n,t,e){return t*(-Math.pow(2,-10*a/e)+1)*1024/1023+n}function u(a){return"number"==typeof a&&!isNaN(a)}var l=this;if(l.version=function(){return"1.9.3"},l.options={useEasing:!0,useGrouping:!0,separator:",",decimal:".",easingFn:s,formattingFn:o,prefix:"",suffix:"",numerals:[]},r&&"object"==typeof r)for(var m in l.options)r.hasOwnProperty(m)&&null!==r[m]&&(l.options[m]=r[m]);""===l.options.separator?l.options.useGrouping=!1:l.options.separator=""+l.options.separator;for(var d=0,c=["webkit","moz","ms","o"],f=0;f<c.length&&!window.requestAnimationFrame;++f)window.requestAnimationFrame=window[c[f]+"RequestAnimationFrame"],window.cancelAnimationFrame=window[c[f]+"CancelAnimationFrame"]||window[c[f]+"CancelRequestAnimationFrame"];window.requestAnimationFrame||(window.requestAnimationFrame=function(a,n){var t=(new Date).getTime(),e=Math.max(0,16-(t-d)),i=window.setTimeout(function(){a(t+e)},e);return d=t+e,i}),window.cancelAnimationFrame||(window.cancelAnimationFrame=function(a){clearTimeout(a)}),l.initialize=function(){return!!l.initialized||(l.error="",l.d="string"==typeof a?document.getElementById(a):a,l.d?(l.startVal=Number(n),l.endVal=Number(t),u(l.startVal)&&u(l.endVal)?(l.decimals=Math.max(0,e||0),l.dec=Math.pow(10,l.decimals),l.duration=1e3*Number(i)||2e3,l.countDown=l.startVal>l.endVal,l.frameVal=l.startVal,l.initialized=!0,!0):(l.error="[CountUp] startVal ("+n+") or endVal ("+t+") is not a number",!1)):(l.error="[CountUp] target is null or undefined",!1))},l.printValue=function(a){var n=l.options.formattingFn(a);"INPUT"===l.d.tagName?this.d.value=n:"text"===l.d.tagName||"tspan"===l.d.tagName?this.d.textContent=n:this.d.innerHTML=n},l.count=function(a){l.startTime||(l.startTime=a),l.timestamp=a;var n=a-l.startTime;l.remaining=l.duration-n,l.options.useEasing?l.countDown?l.frameVal=l.startVal-l.options.easingFn(n,0,l.startVal-l.endVal,l.duration):l.frameVal=l.options.easingFn(n,l.startVal,l.endVal-l.startVal,l.duration):l.countDown?l.frameVal=l.startVal-(l.startVal-l.endVal)*(n/l.duration):l.frameVal=l.startVal+(l.endVal-l.startVal)*(n/l.duration),l.countDown?l.frameVal=l.frameVal<l.endVal?l.endVal:l.frameVal:l.frameVal=l.frameVal>l.endVal?l.endVal:l.frameVal,l.frameVal=Math.round(l.frameVal*l.dec)/l.dec,l.printValue(l.frameVal),n<l.duration?l.rAF=requestAnimationFrame(l.count):l.callback&&l.callback()},l.start=function(a){l.initialize()&&(l.callback=a,l.rAF=requestAnimationFrame(l.count))},l.pauseResume=function(){l.paused?(l.paused=!1,delete l.startTime,l.duration=l.remaining,l.startVal=l.frameVal,requestAnimationFrame(l.count)):(l.paused=!0,cancelAnimationFrame(l.rAF))},l.reset=function(){l.paused=!1,delete l.startTime,l.initialized=!1,l.initialize()&&(cancelAnimationFrame(l.rAF),l.printValue(l.startVal))},l.update=function(a){if(l.initialize()){if(a=Number(a),!u(a))return void(l.error="[CountUp] update() - new endVal is not a number: "+a);l.error="",a!==l.frameVal&&(cancelAnimationFrame(l.rAF),l.paused=!1,delete l.startTime,l.startVal=l.frameVal,l.endVal=a,l.countDown=l.startVal>l.endVal,l.rAF=requestAnimationFrame(l.count))}},l.initialize()&&l.printValue(l.startVal)};return e});\n\n//# sourceURL=webpack:///./node_modules/countup.js/dist/countUp.min.js?')},J7IK:function(module,__webpack_exports__,__webpack_require__){"use strict";eval('/* harmony import */ var react__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__("q1tI");\n/* harmony import */ var react__WEBPACK_IMPORTED_MODULE_0___default = /*#__PURE__*/__webpack_require__.n(react__WEBPACK_IMPORTED_MODULE_0__);\n/* harmony import */ var _assets_logo1_png__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__("U093");\n/* harmony import */ var _assets_logo1_png__WEBPACK_IMPORTED_MODULE_1___default = /*#__PURE__*/__webpack_require__.n(_assets_logo1_png__WEBPACK_IMPORTED_MODULE_1__);\n/* harmony import */ var _assets_logo2_png__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__("X75v");\n/* harmony import */ var _assets_logo2_png__WEBPACK_IMPORTED_MODULE_2___default = /*#__PURE__*/__webpack_require__.n(_assets_logo2_png__WEBPACK_IMPORTED_MODULE_2__);\n/* harmony import */ var _index_less__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__("ZVeW");\n/* harmony import */ var _index_less__WEBPACK_IMPORTED_MODULE_3___default = /*#__PURE__*/__webpack_require__.n(_index_less__WEBPACK_IMPORTED_MODULE_3__);\n\n\n\n\n/* harmony default export */ __webpack_exports__["a"] = (function (_ref) {\n  var _ref$type = _ref.type,\n      type = _ref$type === void 0 ? 1 : _ref$type,\n      _ref$className = _ref.className,\n      className = _ref$className === void 0 ? \'\' : _ref$className,\n      _ref$animate = _ref.animate,\n      animate = _ref$animate === void 0 ? false : _ref$animate;\n  return /*#__PURE__*/react__WEBPACK_IMPORTED_MODULE_0___default.a.createElement("div", {\n    className: "header ".concat(className)\n  }, /*#__PURE__*/react__WEBPACK_IMPORTED_MODULE_0___default.a.createElement("img", {\n    src: type === 1 ? _assets_logo1_png__WEBPACK_IMPORTED_MODULE_1___default.a : _assets_logo2_png__WEBPACK_IMPORTED_MODULE_2___default.a,\n    className: animate ? \'animate__animated animate__pulse\' : \'\'\n  }));\n});\n\n//# sourceURL=webpack:///./src/components/logo/index.jsx?')},"JO/o":function(module,exports,__webpack_require__){eval('module.exports = __webpack_require__.p + "static/arr1.4bd430d0.png";\n\n//# sourceURL=webpack:///./src/assets/arr1.png?')},KGB8:function(module,exports,__webpack_require__){eval("// extracted by mini-css-extract-plugin\n\n//# sourceURL=webpack:///./src/components/arrow/index.less?")},PHNs:function(module,exports,__webpack_require__){"use strict";eval('\n\nObject.defineProperty(exports, \'__esModule\', { value: true });\n\nfunction _interopDefault (ex) { return (ex && (typeof ex === \'object\') && \'default\' in ex) ? ex[\'default\'] : ex; }\n\nvar PropTypes = _interopDefault(__webpack_require__("17x9"));\nvar React = __webpack_require__("q1tI");\nvar React__default = _interopDefault(React);\nvar warning = _interopDefault(__webpack_require__("2W6z"));\nvar CountUp$1 = _interopDefault(__webpack_require__("GiOn"));\n\nfunction _classCallCheck(instance, Constructor) {\n  if (!(instance instanceof Constructor)) {\n    throw new TypeError("Cannot call a class as a function");\n  }\n}\n\nfunction _defineProperties(target, props) {\n  for (var i = 0; i < props.length; i++) {\n    var descriptor = props[i];\n    descriptor.enumerable = descriptor.enumerable || false;\n    descriptor.configurable = true;\n    if ("value" in descriptor) descriptor.writable = true;\n    Object.defineProperty(target, descriptor.key, descriptor);\n  }\n}\n\nfunction _createClass(Constructor, protoProps, staticProps) {\n  if (protoProps) _defineProperties(Constructor.prototype, protoProps);\n  if (staticProps) _defineProperties(Constructor, staticProps);\n  return Constructor;\n}\n\nfunction _defineProperty(obj, key, value) {\n  if (key in obj) {\n    Object.defineProperty(obj, key, {\n      value: value,\n      enumerable: true,\n      configurable: true,\n      writable: true\n    });\n  } else {\n    obj[key] = value;\n  }\n\n  return obj;\n}\n\nfunction ownKeys(object, enumerableOnly) {\n  var keys = Object.keys(object);\n\n  if (Object.getOwnPropertySymbols) {\n    var symbols = Object.getOwnPropertySymbols(object);\n    if (enumerableOnly) symbols = symbols.filter(function (sym) {\n      return Object.getOwnPropertyDescriptor(object, sym).enumerable;\n    });\n    keys.push.apply(keys, symbols);\n  }\n\n  return keys;\n}\n\nfunction _objectSpread2(target) {\n  for (var i = 1; i < arguments.length; i++) {\n    var source = arguments[i] != null ? arguments[i] : {};\n\n    if (i % 2) {\n      ownKeys(Object(source), true).forEach(function (key) {\n        _defineProperty(target, key, source[key]);\n      });\n    } else if (Object.getOwnPropertyDescriptors) {\n      Object.defineProperties(target, Object.getOwnPropertyDescriptors(source));\n    } else {\n      ownKeys(Object(source)).forEach(function (key) {\n        Object.defineProperty(target, key, Object.getOwnPropertyDescriptor(source, key));\n      });\n    }\n  }\n\n  return target;\n}\n\nfunction _inherits(subClass, superClass) {\n  if (typeof superClass !== "function" && superClass !== null) {\n    throw new TypeError("Super expression must either be null or a function");\n  }\n\n  subClass.prototype = Object.create(superClass && superClass.prototype, {\n    constructor: {\n      value: subClass,\n      writable: true,\n      configurable: true\n    }\n  });\n  if (superClass) _setPrototypeOf(subClass, superClass);\n}\n\nfunction _getPrototypeOf(o) {\n  _getPrototypeOf = Object.setPrototypeOf ? Object.getPrototypeOf : function _getPrototypeOf(o) {\n    return o.__proto__ || Object.getPrototypeOf(o);\n  };\n  return _getPrototypeOf(o);\n}\n\nfunction _setPrototypeOf(o, p) {\n  _setPrototypeOf = Object.setPrototypeOf || function _setPrototypeOf(o, p) {\n    o.__proto__ = p;\n    return o;\n  };\n\n  return _setPrototypeOf(o, p);\n}\n\nfunction _assertThisInitialized(self) {\n  if (self === void 0) {\n    throw new ReferenceError("this hasn\'t been initialised - super() hasn\'t been called");\n  }\n\n  return self;\n}\n\nfunction _possibleConstructorReturn(self, call) {\n  if (call && (typeof call === "object" || typeof call === "function")) {\n    return call;\n  }\n\n  return _assertThisInitialized(self);\n}\n\nfunction _slicedToArray(arr, i) {\n  return _arrayWithHoles(arr) || _iterableToArrayLimit(arr, i) || _nonIterableRest();\n}\n\nfunction _arrayWithHoles(arr) {\n  if (Array.isArray(arr)) return arr;\n}\n\nfunction _iterableToArrayLimit(arr, i) {\n  if (!(Symbol.iterator in Object(arr) || Object.prototype.toString.call(arr) === "[object Arguments]")) {\n    return;\n  }\n\n  var _arr = [];\n  var _n = true;\n  var _d = false;\n  var _e = undefined;\n\n  try {\n    for (var _i = arr[Symbol.iterator](), _s; !(_n = (_s = _i.next()).done); _n = true) {\n      _arr.push(_s.value);\n\n      if (i && _arr.length === i) break;\n    }\n  } catch (err) {\n    _d = true;\n    _e = err;\n  } finally {\n    try {\n      if (!_n && _i["return"] != null) _i["return"]();\n    } finally {\n      if (_d) throw _e;\n    }\n  }\n\n  return _arr;\n}\n\nfunction _nonIterableRest() {\n  throw new TypeError("Invalid attempt to destructure non-iterable instance");\n}\n\nvar createCountUpInstance = function createCountUpInstance(el, props) {\n  var decimal = props.decimal,\n      decimals = props.decimals,\n      duration = props.duration,\n      easingFn = props.easingFn,\n      end = props.end,\n      formattingFn = props.formattingFn,\n      prefix = props.prefix,\n      separator = props.separator,\n      start = props.start,\n      suffix = props.suffix,\n      useEasing = props.useEasing;\n  return new CountUp$1(el, start, end, decimals, duration, {\n    decimal: decimal,\n    easingFn: easingFn,\n    formattingFn: formattingFn,\n    separator: separator,\n    prefix: prefix,\n    suffix: suffix,\n    useEasing: useEasing,\n    useGrouping: !!separator\n  });\n};\n\nvar CountUp =\n/*#__PURE__*/\nfunction (_Component) {\n  _inherits(CountUp, _Component);\n\n  function CountUp() {\n    var _getPrototypeOf2;\n\n    var _this;\n\n    _classCallCheck(this, CountUp);\n\n    for (var _len = arguments.length, args = new Array(_len), _key = 0; _key < _len; _key++) {\n      args[_key] = arguments[_key];\n    }\n\n    _this = _possibleConstructorReturn(this, (_getPrototypeOf2 = _getPrototypeOf(CountUp)).call.apply(_getPrototypeOf2, [this].concat(args)));\n\n    _defineProperty(_assertThisInitialized(_this), "createInstance", function () {\n      if (typeof _this.props.children === \'function\') {\n        // Warn when user didn\'t use containerRef at all\n        warning(_this.containerRef.current && (_this.containerRef.current instanceof HTMLElement || _this.containerRef.current instanceof SVGTextElement || _this.containerRef.current instanceof SVGTSpanElement), "Couldn\'t find attached element to hook the CountUp instance into! Try to attach \\"containerRef\\" from the render prop to a an HTMLElement, eg. <span ref={containerRef} />.");\n      }\n\n      return createCountUpInstance(_this.containerRef.current, _this.props);\n    });\n\n    _defineProperty(_assertThisInitialized(_this), "pauseResume", function () {\n      var _assertThisInitialize = _assertThisInitialized(_this),\n          reset = _assertThisInitialize.reset,\n          start = _assertThisInitialize.restart,\n          update = _assertThisInitialize.update;\n\n      var onPauseResume = _this.props.onPauseResume;\n\n      _this.instance.pauseResume();\n\n      onPauseResume({\n        reset: reset,\n        start: start,\n        update: update\n      });\n    });\n\n    _defineProperty(_assertThisInitialized(_this), "reset", function () {\n      var _assertThisInitialize2 = _assertThisInitialized(_this),\n          pauseResume = _assertThisInitialize2.pauseResume,\n          start = _assertThisInitialize2.restart,\n          update = _assertThisInitialize2.update;\n\n      var onReset = _this.props.onReset;\n\n      _this.instance.reset();\n\n      onReset({\n        pauseResume: pauseResume,\n        start: start,\n        update: update\n      });\n    });\n\n    _defineProperty(_assertThisInitialized(_this), "restart", function () {\n      _this.reset();\n\n      _this.start();\n    });\n\n    _defineProperty(_assertThisInitialized(_this), "start", function () {\n      var _assertThisInitialize3 = _assertThisInitialized(_this),\n          pauseResume = _assertThisInitialize3.pauseResume,\n          reset = _assertThisInitialize3.reset,\n          start = _assertThisInitialize3.restart,\n          update = _assertThisInitialize3.update;\n\n      var _this$props = _this.props,\n          delay = _this$props.delay,\n          onEnd = _this$props.onEnd,\n          onStart = _this$props.onStart;\n\n      var run = function run() {\n        return _this.instance.start(function () {\n          return onEnd({\n            pauseResume: pauseResume,\n            reset: reset,\n            start: start,\n            update: update\n          });\n        });\n      }; // Delay start if delay prop is properly set\n\n\n      if (delay > 0) {\n        _this.timeoutId = setTimeout(run, delay * 1000);\n      } else {\n        run();\n      }\n\n      onStart({\n        pauseResume: pauseResume,\n        reset: reset,\n        update: update\n      });\n    });\n\n    _defineProperty(_assertThisInitialized(_this), "update", function (newEnd) {\n      var _assertThisInitialize4 = _assertThisInitialized(_this),\n          pauseResume = _assertThisInitialize4.pauseResume,\n          reset = _assertThisInitialize4.reset,\n          start = _assertThisInitialize4.restart;\n\n      var onUpdate = _this.props.onUpdate;\n\n      _this.instance.update(newEnd);\n\n      onUpdate({\n        pauseResume: pauseResume,\n        reset: reset,\n        start: start\n      });\n    });\n\n    _defineProperty(_assertThisInitialized(_this), "containerRef", React__default.createRef());\n\n    return _this;\n  }\n\n  _createClass(CountUp, [{\n    key: "componentDidMount",\n    value: function componentDidMount() {\n      var _this$props2 = this.props,\n          children = _this$props2.children,\n          delay = _this$props2.delay;\n      this.instance = this.createInstance(); // Don\'t invoke start if component is used as a render prop\n\n      if (typeof children === \'function\' && delay !== 0) return; // Otherwise just start immediately\n\n      this.start();\n    }\n  }, {\n    key: "shouldComponentUpdate",\n    value: function shouldComponentUpdate(nextProps) {\n      var _this$props3 = this.props,\n          end = _this$props3.end,\n          start = _this$props3.start,\n          suffix = _this$props3.suffix,\n          prefix = _this$props3.prefix,\n          redraw = _this$props3.redraw,\n          duration = _this$props3.duration,\n          separator = _this$props3.separator,\n          decimals = _this$props3.decimals,\n          decimal = _this$props3.decimal;\n      var hasCertainPropsChanged = duration !== nextProps.duration || end !== nextProps.end || start !== nextProps.start || suffix !== nextProps.suffix || prefix !== nextProps.prefix || separator !== nextProps.separator || decimals !== nextProps.decimals || decimal !== nextProps.decimal;\n      return hasCertainPropsChanged || redraw;\n    }\n  }, {\n    key: "componentDidUpdate",\n    value: function componentDidUpdate(prevProps) {\n      // If duration, suffix, prefix, separator or start has changed\n      // there\'s no way to update the values.\n      // So we need to re-create the CountUp instance in order to\n      // restart it.\n      var _this$props4 = this.props,\n          end = _this$props4.end,\n          start = _this$props4.start,\n          suffix = _this$props4.suffix,\n          prefix = _this$props4.prefix,\n          duration = _this$props4.duration,\n          separator = _this$props4.separator,\n          decimals = _this$props4.decimals,\n          decimal = _this$props4.decimal,\n          preserveValue = _this$props4.preserveValue;\n\n      if (duration !== prevProps.duration || start !== prevProps.start || suffix !== prevProps.suffix || prefix !== prevProps.prefix || separator !== prevProps.separator || decimals !== prevProps.decimals || decimal !== prevProps.decimal) {\n        this.instance.reset();\n        this.instance = this.createInstance();\n        this.start();\n      } // Only end value has changed, so reset and and re-animate with the updated\n      // end value.\n\n\n      if (end !== prevProps.end) {\n        if (!preserveValue) {\n          this.instance.reset();\n        }\n\n        this.instance.update(end);\n      }\n    }\n  }, {\n    key: "componentWillUnmount",\n    value: function componentWillUnmount() {\n      if (this.timeoutId) {\n        clearTimeout(this.timeoutId);\n      }\n\n      this.instance.reset();\n    }\n  }, {\n    key: "render",\n    value: function render() {\n      var _this$props5 = this.props,\n          children = _this$props5.children,\n          className = _this$props5.className,\n          style = _this$props5.style;\n      var containerRef = this.containerRef,\n          pauseResume = this.pauseResume,\n          reset = this.reset,\n          restart = this.restart,\n          update = this.update;\n\n      if (typeof children === \'function\') {\n        return children({\n          countUpRef: containerRef,\n          pauseResume: pauseResume,\n          reset: reset,\n          start: restart,\n          update: update\n        });\n      }\n\n      return React__default.createElement("span", {\n        className: className,\n        ref: containerRef,\n        style: style\n      });\n    }\n  }]);\n\n  return CountUp;\n}(React.Component);\n\n_defineProperty(CountUp, "propTypes", {\n  decimal: PropTypes.string,\n  decimals: PropTypes.number,\n  delay: PropTypes.number,\n  easingFn: PropTypes.func,\n  end: PropTypes.number.isRequired,\n  formattingFn: PropTypes.func,\n  onEnd: PropTypes.func,\n  onStart: PropTypes.func,\n  prefix: PropTypes.string,\n  redraw: PropTypes.bool,\n  separator: PropTypes.string,\n  start: PropTypes.number,\n  startOnMount: PropTypes.bool,\n  suffix: PropTypes.string,\n  style: PropTypes.object,\n  useEasing: PropTypes.bool,\n  preserveValue: PropTypes.bool\n});\n\n_defineProperty(CountUp, "defaultProps", {\n  decimal: \'.\',\n  decimals: 0,\n  delay: null,\n  duration: null,\n  easingFn: null,\n  formattingFn: null,\n  onEnd: function onEnd() {},\n  onPauseResume: function onPauseResume() {},\n  onReset: function onReset() {},\n  onStart: function onStart() {},\n  onUpdate: function onUpdate() {},\n  prefix: \'\',\n  redraw: false,\n  separator: \'\',\n  start: 0,\n  startOnMount: true,\n  suffix: \'\',\n  style: undefined,\n  useEasing: true,\n  preserveValue: false\n});\n\n// and just sets the innerHTML of the element.\n\nvar MOCK_ELEMENT = {\n  innerHTML: null\n};\n\nvar useCountUp = function useCountUp(props) {\n  var _props = _objectSpread2({}, CountUp.defaultProps, {}, props);\n\n  var start = _props.start,\n      formattingFn = _props.formattingFn;\n\n  var _useState = React.useState(typeof formattingFn === \'function\' ? formattingFn(start) : start),\n      _useState2 = _slicedToArray(_useState, 2),\n      count = _useState2[0],\n      setCount = _useState2[1];\n\n  var countUpRef = React.useRef(null);\n\n  var createInstance = function createInstance() {\n    var countUp = createCountUpInstance(MOCK_ELEMENT, _props);\n    var formattingFnRef = countUp.options.formattingFn;\n\n    countUp.options.formattingFn = function () {\n      var result = formattingFnRef.apply(void 0, arguments);\n      setCount(result);\n    };\n\n    return countUp;\n  };\n\n  var getCountUp = function getCountUp() {\n    var countUp = countUpRef.current;\n\n    if (countUp !== null) {\n      return countUp;\n    }\n\n    var newCountUp = createInstance();\n    countUpRef.current = newCountUp;\n    return newCountUp;\n  };\n\n  var reset = function reset() {\n    var onReset = _props.onReset;\n    getCountUp().reset();\n    onReset({\n      pauseResume: pauseResume,\n      start: restart,\n      update: update\n    });\n  };\n\n  var restart = function restart() {\n    var onStart = _props.onStart,\n        onEnd = _props.onEnd;\n    getCountUp().reset();\n    getCountUp().start(function () {\n      onEnd({\n        pauseResume: pauseResume,\n        reset: reset,\n        start: restart,\n        update: update\n      });\n    });\n    onStart({\n      pauseResume: pauseResume,\n      reset: reset,\n      update: update\n    });\n  };\n\n  var pauseResume = function pauseResume() {\n    var onPauseResume = _props.onPauseResume;\n    getCountUp().pauseResume();\n    onPauseResume({\n      reset: reset,\n      start: restart,\n      update: update\n    });\n  };\n\n  var update = function update(newEnd) {\n    var onUpdate = _props.onUpdate;\n    getCountUp().update(newEnd);\n    onUpdate({\n      pauseResume: pauseResume,\n      reset: reset,\n      start: restart\n    });\n  };\n\n  React.useEffect(function () {\n    var delay = _props.delay,\n        onStart = _props.onStart,\n        onEnd = _props.onEnd,\n        startOnMount = _props.startOnMount;\n\n    if (startOnMount) {\n      var timeout = setTimeout(function () {\n        onStart({\n          pauseResume: pauseResume,\n          reset: reset,\n          update: update\n        });\n        getCountUp().start(function () {\n          clearTimeout(timeout);\n          onEnd({\n            pauseResume: pauseResume,\n            reset: reset,\n            start: restart,\n            update: update\n          });\n        });\n      }, delay * 1000);\n    }\n\n    return reset;\n  }, []);\n  return {\n    countUp: count,\n    start: restart,\n    pauseResume: pauseResume,\n    reset: reset,\n    update: update\n  };\n};\n\nexports.default = CountUp;\nexports.useCountUp = useCountUp;\n\n\n//# sourceURL=webpack:///./node_modules/react-countup/build/index.js?')},U093:function(module,exports,__webpack_require__){eval('module.exports = __webpack_require__.p + "static/logo1.1c5b2a0b.png";\n\n//# sourceURL=webpack:///./src/assets/logo1.png?')},X75v:function(module,exports,__webpack_require__){eval('module.exports = __webpack_require__.p + "static/logo2.cc98e33e.png";\n\n//# sourceURL=webpack:///./src/assets/logo2.png?')},ZVeW:function(module,exports,__webpack_require__){eval("// extracted by mini-css-extract-plugin\n\n//# sourceURL=webpack:///./src/components/logo/index.less?")},gDDM:function(module,exports,__webpack_require__){eval('module.exports = __webpack_require__.p + "static/m.98dac35c.png";\n\n//# sourceURL=webpack:///./src/assets/page3/m.png?')},gIJL:function(module,exports,__webpack_require__){eval('module.exports = __webpack_require__.p + "static/w.46fb6e5d.png";\n\n//# sourceURL=webpack:///./src/assets/page3/w.png?')},y8iU:function(module,__webpack_exports__,__webpack_require__){"use strict";eval('/* harmony import */ var react__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__("q1tI");\n/* harmony import */ var react__WEBPACK_IMPORTED_MODULE_0___default = /*#__PURE__*/__webpack_require__.n(react__WEBPACK_IMPORTED_MODULE_0__);\n/* harmony import */ var _assets_arr1_png__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__("JO/o");\n/* harmony import */ var _assets_arr1_png__WEBPACK_IMPORTED_MODULE_1___default = /*#__PURE__*/__webpack_require__.n(_assets_arr1_png__WEBPACK_IMPORTED_MODULE_1__);\n/* harmony import */ var _assets_arr2_png__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__("+5p2");\n/* harmony import */ var _assets_arr2_png__WEBPACK_IMPORTED_MODULE_2___default = /*#__PURE__*/__webpack_require__.n(_assets_arr2_png__WEBPACK_IMPORTED_MODULE_2__);\n/* harmony import */ var _index_less__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__("KGB8");\n/* harmony import */ var _index_less__WEBPACK_IMPORTED_MODULE_3___default = /*#__PURE__*/__webpack_require__.n(_index_less__WEBPACK_IMPORTED_MODULE_3__);\n\n\n\n\n/* harmony default export */ __webpack_exports__["a"] = (function (_ref) {\n  var _ref$arr = _ref.arr,\n      arr = _ref$arr === void 0 ? 1 : _ref$arr;\n  return /*#__PURE__*/react__WEBPACK_IMPORTED_MODULE_0___default.a.createElement("div", {\n    className: "arrow"\n  }, /*#__PURE__*/react__WEBPACK_IMPORTED_MODULE_0___default.a.createElement("img", {\n    src: arr === 1 ? _assets_arr1_png__WEBPACK_IMPORTED_MODULE_1___default.a : _assets_arr2_png__WEBPACK_IMPORTED_MODULE_2___default.a,\n    className: "animate__animated animate__slow animate__fadeInDown animate__infinite"\n  }));\n});\n\n//# sourceURL=webpack:///./src/components/arrow/index.jsx?')}}]);