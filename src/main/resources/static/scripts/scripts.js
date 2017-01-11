"use strict";
var resolve = {
    delay: function ($q, $timeout) {
        var delay = $q.defer();
        return $timeout(delay.resolve, 0, !1), delay.promise
    }
};
angular.module("dropApp", ["ngCookies", "ngResource", "ngRoute", "ngSanitize", "ngProgress", "timer", "infinite-scroll"]).config(["$compileProvider", function ($compileProvider) {
    $compileProvider.debugInfoEnabled(!1)
}]).config(["$routeProvider", "$locationProvider", "$httpProvider", function ($routeProvider, $locationProvider, $httpProvider) {
    $routeProvider.when("/", {
        templateUrl: "views/main.html",
        controller: "MainCtrl",
        resolve: resolve
    }).when("/all", {
        templateUrl: "views/all.html",
        controller: "AllCtrl",
        resolve: resolve
    }).when("/create", {
        templateUrl: "views/create.html",
        controller: "CreateCtrl",
        resolve: resolve
    }).when("/featured", {
        templateUrl: "views/drop.html",
        controller: "DropCtrl",
        reloadOnSearch: !1,
        resolve: resolve
    }).when("/drop/:id", {
        templateUrl: "views/drop.html",
        controller: "DropCtrl",
        reloadOnSearch: !1,
        resolve: resolve
    }).otherwise({redirectTo: "/"}), $httpProvider.defaults.headers.post["Content-Type"] = "application/x-www-form-urlencoded;charset=utf-8";
    var param = function (obj) {
        var name, value, fullSubName, subName, subValue, innerObj, i, query = "";
        for (name in obj)if (value = obj[name], value instanceof Array)for (i = 0; i < value.length; ++i)subValue = value[i], fullSubName = name + "[" + i + "]", innerObj = {}, innerObj[fullSubName] = subValue, query += param(innerObj) + "&"; else if (value instanceof Object)for (subName in value)subValue = value[subName], fullSubName = name + "[" + subName + "]", innerObj = {}, innerObj[fullSubName] = subValue, query += param(innerObj) + "&"; else void 0 !== value && null !== value && (query += encodeURIComponent(name) + "=" + encodeURIComponent(value) + "&");
        return query.length ? query.substr(0, query.length - 1) : query
    };
    $httpProvider.defaults.transformRequest = [function (data) {
        return angular.isObject(data) && "[object File]" !== String(data) ? param(data) : data
    }]
}]).run(["$rootScope", "$location", "$route", "$timeout", "Tables", "ngProgress", function ($rootScope, $location, $route, $timeout, Tables, ngProgress) {
/*
    var token_key = "access_token=", token_index = window.location.href.indexOf(token_key);
    if (token_index > -1) {
        var token = window.location.href.substr(token_index + token_key.length).split("&")[0];
        Tables.set_access_token(token), $location.path("/create")
    }
*/
    var oldLocation = "";
    $rootScope.$on("$routeChangeStart", function (angularEvent, next) {
        $rootScope.view_class = "view-" + $location.path().substr(1), ngProgress.reset();
        var isForwards = !0;
        if (next && next.$$route) {
            var newLocation = next.$$route.originalPath;
            oldLocation !== newLocation && -1 !== oldLocation.indexOf(newLocation) && (isForwards = !1), oldLocation = newLocation
        }
    }), ngProgress.color("#16e58a")
}]), angular.module("dropApp").service("Image", function () {
    function hexToRgb(hex) {
        var result = /^#?([a-f\d]{2})([a-f\d]{2})([a-f\d]{2})$/i.exec(hex);
        return result ? {r: parseInt(result[1], 16), g: parseInt(result[2], 16), b: parseInt(result[3], 16)} : null
    }

    this.grayscale = function (pixels) {
        for (var d = pixels.data, i = 0; i < d.length; i += 4) {
            var r = d[i], g = d[i + 1], b = d[i + 2], v = .2126 * r + .7152 * g + .0722 * b;
            d[i] = d[i + 1] = d[i + 2] = v
        }
        return pixels
    }, this.duotone = function (img, tone1, tone2) {
        var gradient = this.gradientMap(tone1, tone2);
        img = this.grayscale(img);
        for (var d = img.data, i = 0; i < d.length; i += 4)d[i] = gradient[4 * d[i]], d[i + 1] = gradient[4 * d[i + 1] + 1], d[i + 2] = gradient[4 * d[i + 2] + 2];
        return img
    }, this.gradientMap = function (tone1, tone2) {
        for (var rgb1 = hexToRgb(tone1), rgb2 = hexToRgb(tone2), gradient = [], i = 0; 1024 > i; i += 4)gradient[i] = ((256 - i / 4) * rgb1.r + i / 4 * rgb2.r) / 256, gradient[i + 1] = ((256 - i / 4) * rgb1.g + i / 4 * rgb2.g) / 256, gradient[i + 2] = ((256 - i / 4) * rgb1.b + i / 4 * rgb2.b) / 256, gradient[i + 3] = 255;
        return gradient
    }, this.flipImage = function (input) {
        for (var tempCtx = document.createElement("canvas").getContext("2d"), output = tempCtx.createImageData(input.width, input.height), w = input.width, h = input.height, inputData = input.data, outputData = output.data, y = 1; h - 1 > y; y += 1)for (var x = 1; w - 1 > x; x += 1)for (var i = 4 * (y * w + x), flip = 4 * (y * w + (w - x)), c = 0; 4 > c; c += 1)outputData[i + c] = inputData[flip + c];
        return output
    }
}), angular.module("dropApp").service("BlockFormatter", function () {
    this.format_media = function (media, parent) {
        var block = {};
        switch (block.type = "media", block.media_type = media.drop_type, block.title = media.title ? media.title : "Check out this exclusive " + media.drop_type + "!", block.description = media.description ? media.description : "Love " + parent.name + "? Don't miss this!", block.url = "/drop/" + media.artist_id + "?media=" + media.id, media.drop_type) {
            case"video":
                block.call_to_action = "watch now";
                break;
            case"gallery":
                block.call_to_action = "gallery";
                break;
            case"playlist":
                block.call_to_action = "follow"
        }
        return block
    }, this.format_drop = function (data) {
        var block = {};
        return block.type = "drop", block.title = data.name, block.image = data.files[data.featured_image_id].url, block.url = "/drop/" + data.id, block.call_to_action = "view drop", block
    }
}), angular.module("dropApp").service("Tables", ["$http", "$location", "$window", "$q", function ($http, $location, $window, $q) {
    var access_token, user, api_base = "https://api.spotify.com/v1";
	this.get_user = function () {
		alert('TEST');
        var deferred = $q.defer();
        return user ? deferred.resolve(user) : $http({
                url: api_base + "/me",
                headers: {Authorization: "Bearer " + access_token}
            }).success(function (response) {
                deferred.resolve(user = response)
            }).error(function (response) {
                deferred.reject()
            }), deferred.promise
    }
}]), angular.module("dropApp").service("Api", ["$http", "$q", function ($http, $q) {
    function card_shim(raw) {
        for (var i = 0; i < raw.length; i++)raw[i].type = "card";
        return raw
    }

    function drop_shim(data) {
        function get_image_url(id) {
            return data.files[id] ? data.files[id].url : null
        }

        function get_image_content_url(i, id) {
            return data.content[i].files[id] ? data.content[i].files[id].url : null
        }

        var now = (new Date).getTime();
        data.type = "drop", data.featured_image = get_image_url(data.featured_image_id), data.header_image = get_image_url(data.header_image_id), data.dropped_at = moment(data.reveal_date).fromNow(), data.single_uri = "spotify:track:" + data.single, data.dropped = moment(data.release_date).isBefore(now), "i" === data.dropped_at.charAt(0) && "n" === data.dropped_at.charAt(1) ? data.dropped_at = "Dropping " + data.dropped_at : data.dropped_at = "Dropped " + data.dropped_at, data.card_images = [];
        var card_image_1 = get_image_url(data.artist_image1_id), card_image_2 = get_image_url(data.artist_image2_id), card_image_3 = get_image_url(data.artist_image3_id);
        if (card_image_1 && data.card_images.push(card_image_1), card_image_2 && data.card_images.push(card_image_2), card_image_3 && data.card_images.push(card_image_3), data.content)for (var i = 0; i < data.content.length; i++) {
            var content = data.content[i];
            content.item_index = i, content.type = "media", content.media_type = content.content_type, content.media_index = i, content.featured_image = get_image_content_url(i, content.featured_image_id), content.dropped_at = moment(content.release_date).fromNow(), content.drop_id = data.id, content.locked = moment(content.release_date).isAfter(now)
        }
        return data
    }

    var drops, cards, ads, base = "http://localhost:8080";
    this.get_drop = function (id) {
/*
		alert("getDrop");
        var deferred = $q.defer();
        return $http.get(base + "/drop/" + id + "/").success(function (response) {
            deferred.resolve(drop_shim(response))
        }), deferred.promise
*/
    }, this.get_all_drops = function () {
/*
	    alert("getalldrops");
        var deferred = $q.defer();
        return drops ? deferred.resolve(drops) : $http.get(base + "/drops").success(function (response) {
                for (var i = 0; i < response.length; i++)response[i] = drop_shim(response[i]);
                deferred.resolve(drops = response)
            }), deferred.promise
*/
    }, this.get_featured = function () {
        var deferred = $q.defer();

        var prefixPos = location.search.toString().indexOf("userId=");
        var dataUrl = base + "/drops?featured=1";
        var hasUserId = false;

        if (prefixPos != -1) {
            hasUserId = true;
            var userId = location.search.toString().substr(prefixPos + "userId=".length);
            dataUrl = "/get-appointment?GmailId=" + userId;
        }
        return $http.get(dataUrl).success(function (response) {
            // if (response == null){
            //     response = defaultDrop;
            // }
            console.log("result: %o", response);
            if (response != null && response.users != null){
                var result = defaultDrop;
                //
                // for (var i = 0; i < response.users.length; i++){
                //     result[i + 1].description = response.users[i].name;
                //     result[i + 1]. = response.users[i].name;
                // }
            }
            response = defaultDrop;
            deferred.resolve(drop_shim(response[response.length - 1]))
        }), deferred.promise
    }, this.get_cards = function () {
        var deferred = $q.defer();
        return cards ? deferred.resolve(cards) : $http.get(base + "/cards?featured=1/").success(function (response) {
                cards = card_shim(response), deferred.resolve(cards)
            }), deferred.promise
    }, this.get_single_card = function (route) {
        var deferred = $q.defer();
        return cards ? deferred.resolve(cards) : $http.get(route).success(function (response) {
                deferred.resolve(response)
            }), deferred.promise
    }, this.get_ad = function () {
        var deferred = $q.defer(), addArray = [];
        return ads ? deferred.resolve(ads) : $http.get(base + "/brands").success(function (response) {
                if ("object" == typeof response) {
                    var adObj = {
                        type: "ad",
                        image: "images/mc-ad-blue.jpg",
                        description: "It takes two to tango with a 20 piece.",
                        content_url: ""
                    }, add_timestamp = function (url) {
                        return url ? String(url).replace("[timestamp]", (new Date).getTime()) : ""
                    }, setObj = function (responseObj) {
                        adObj.image = responseObj.files[responseObj.image_id].url, adObj.description = responseObj.title, adObj.content_url = responseObj.content_url, adObj.ad_url = add_timestamp(responseObj.ad_url), adObj.ad_text_url = add_timestamp(responseObj.ad_text_url), adObj.ad_text_url || (adObj.ad_text_url = adObj.ad_url), adObj.tracking_pixel_url = add_timestamp(responseObj.tracking_pixel_url), addArray.push(angular.copy(adObj))
                    };
                    response = response.sort(function (a, b) {
                        return a.id == b.id ? 0 : a.id > b.id ? -1 : 1
                    });
                    for (var i = 0; i < response.length; i++)setObj(response[i])
                }
                deferred.resolve(addArray)
            }), deferred.promise
    }, this.save_card = function (card_data, image) {
        var deferred = $q.defer(), data = {drop_id: card_data.drop.id, creator_id: card_data.user.id, image: image};
        return $http({
            method: "POST",
            url: base + "/card/",
            data: data,
            headers: {"Content-Type": "application/x-www-form-urlencoded"}
        }).success(function (response, status, headers, config) {
            deferred.resolve("" + base + headers().location)
        }), deferred.promise
    }
}]), angular.module("dropApp").controller("MainCtrl", ["$scope", "$timeout", "$q", "$location", "Api", "ngProgress", function ($scope, $timeout, $q, $location, Api, ngProgress) {
    $scope.items = [];
    var featured = 0;
    $scope.selectFeature = function (setFeature) {
        featured = setFeature
    }, $scope.isFeatured = function (checkFeature) {
        return featured === checkFeature
    }, this.load_featured_drop = function () {
        Api.get_featured().then(function (response) {
            ngProgress.complete(), $scope.featured = response, $scope.$on("imageLoaded", function () {
                $timeout(function () {
                    $scope.backgroundLoaded = !0, $scope.firstLoad = !0;
                    media:for (var i = $scope.featured.content.length; i--; i >= 0)if (!$scope.featured.content[i].locked) {
                        $scope.items.unshift($scope.featured.content[i]), $scope.items[0].name = $scope.featured.name, $timeout(function () {
                            $scope.firstLoad = !1
                        }, 1e3);
                        break media
                    }
                    $timeout(function () {
                        //Api.get_ad().then(function (response) {
                        //    $scope.items.push(response[0])}
                        //), $scope.ready = !0
                    }, 1600)
                }, 700)
            })
        })
    };
    var random_drops, ad_blocks, counter = 0;
    $scope.load_random_assets = function () {
        ngProgress.start(), $q.all([Api.get_all_drops(), Api.get_ad()]).then(function (response) {
            ngProgress.complete(), random_drops = angular.copy(response[0]), ad_blocks = angular.copy(response[1]), $scope.add_random_assets()
        })
    }, $scope.add_random_assets = function () {
        if (counter++, $scope.ready = !1, !random_drops || !random_drops.length)return $scope.load_random_assets(), !1;
        for (var random_items = [], random_index = Math.floor(Math.random() * (random_drops.length - 9)), random_range = random_drops.splice(random_index, 10), i = 0; i < random_range.length; i++) {
            var selected_drop = random_range[i], selected_drop_media_length = selected_drop.content ? selected_drop.content.length : 0, selected_random_index = Math.floor(Math.random() * (selected_drop_media_length + 1));
            selected_random_index === selected_drop_media_length ? random_items.push(selected_drop) : (selected_drop.content[selected_random_index].name = selected_drop.name, random_items.push(selected_drop.content[selected_random_index]))
        }
        if (counter % 2 === 0) {
            var shuffle = function (a) {
                var j, x, i;
                for (i = a.length; i; i -= 1)j = Math.floor(Math.random() * i), x = a[i - 1], a[i - 1] = a[j], a[j] = x;
                return a
            };
            random_items = shuffle(random_items);
            var shuffle_merge = function (source, target) {
                for (var sourceInds = [], result = [], i = 0; i < source.length; i++)sourceInds.push(i);
                sourceInds = shuffle(shuffle(sourceInds)).slice(0, target.length).sort();
                for (var currentInd = sourceInds.shift(), i = 0; i < source.length; i++)i == currentInd && (result.push(target.pop()), currentInd = sourceInds.shift()), result.push(source[i]);
                return result
            };
            random_items = shuffle_merge(random_items, ad_blocks)
        }
        $scope.items = $scope.items.concat(random_items), $scope.ready = !0
    }, this.load_featured_drop()
}]), angular.module("dropApp").controller("HeaderCtrl", ["$scope", "$location", function ($scope, $location) {
    $scope.links = [{title: "FEATURED", path: "/drop/featured", color: "#fae62d"}, {
        title: "ALL",
        path: "/all",
        color: "#9bf0e1"
    }], $scope.timestamp = (new Date).getTime(), $scope.visisble = !0, $scope.toggleShare = function (toggle) {
        var $shareButtons = $(".share-button"), buttonsNum = $shareButtons.length, buttonsMid = buttonsNum / 3, spacing = 90;
        $scope.visisble && "close" !== toggle ? ($shareButtons.each(function (i) {
                var $cur = $(this), pos = i - 1, dist = Math.abs(pos);
                $cur.css({zIndex: buttonsMid - dist}), TweenMax.to($cur, 1.1 * dist, {
                    x: pos * spacing,
                    y: 90,
                    scale: .95,
                    width: 60,
                    height: 60,
                    ease: Elastic.easeOut,
                    easeParams: [1.01, .5]
                }), TweenMax.to($cur, .8, {
                    delay: .2 * dist - .1,
                    scale: .95,
                    width: 60,
                    height: 60,
                    ease: Elastic.easeOut,
                    easeParams: [1.1, .6]
                }), TweenMax.fromTo($cur.children(".share-icon"), .2, {scale: 0}, {
                    delay: .2 * dist - .1,
                    scale: 1,
                    ease: Quad.easeInOut
                })
            }), $scope.visisble = !$scope.visisble) : "close" === toggle ? ($shareButtons.each(function (i) {
                    var $cur = $(this), pos = i, dist = Math.abs(pos);
                    $cur.css({zIndex: dist}), TweenMax.to($cur, .4 + .1 * (buttonsMid - dist), {
                        x: 0,
                        y: 0,
                        scale: 0,
                        width: 32,
                        height: 32,
                        ease: Quad.easeInOut
                    }), TweenMax.to($cur.children(".share-icon"), .2, {scale: 0, ease: Quad.easeIn})
                }), $scope.visisble = !0) : ($shareButtons.each(function (i) {
                    var $cur = $(this), pos = i, dist = Math.abs(pos);
                    $cur.css({zIndex: dist}), TweenMax.to($cur, .4 + .1 * (buttonsMid - dist), {
                        x: 0,
                        y: 0,
                        scale: 0,
                        width: 32,
                        height: 32,
                        ease: Quad.easeInOut
                    }), TweenMax.to($cur.children(".share-icon"), .2, {scale: 0, ease: Quad.easeIn})
                }), $scope.visisble = !0)
    }, $scope.$on("$locationChangeSuccess", function (e, next) {
        $scope.path = $location.path(), $scope.toggleShare("close")
    }), $scope.path = $location.path()
}]), angular.module("dropApp").controller("FeaturedCtrl", ["$scope", "$timeout", "$q", "$location", "Api", "ngProgress", function ($scope, $timeout, $q, $location, Api, ngProgress) {
    $scope.items = [], this.load_featured_drop = function () {
        ngProgress.start(), Api.get_featured().then(function (response) {
            ngProgress.complete(), $timeout(function () {
                $scope.featured = response;
                media:for (var i = $scope.quantity; i--; i >= 0)if (!$scope.featured.content[i].locked) {
                    $scope.items.unshift($scope.featured.content[i]);
                    break media
                }
                Api.get_ad().then(function (response) {
                    $scope.items.push(response[0])
                }), $scope.ready = !0
            }, 500)
        })
    };
    var random_drops, random_cards;
    $scope.load_random_assets = function () {
        ngProgress.start(), $q.all([Api.get_all_drops(), Api.get_cards()]).then(function (response) {
            ngProgress.complete(), random_drops = angular.copy(response[0]), random_cards = angular.copy(response[1]), $scope.add_random_assets()
        })
    }, $scope.add_random_assets = function () {
        if ($scope.ready = !1, !random_drops || !random_drops.length)return $scope.load_random_assets(), !1;
        for (var random_items = [], random_index = Math.floor(Math.random() * (random_drops.length - 4)), random_range = random_drops.splice(random_index, 5), i = 0; i < random_range.length; i++) {
            var selected_drop = random_range[i], selected_drop_media_length = selected_drop.content ? selected_drop.content.length : 0, selected_random_index = Math.floor(Math.random() * (selected_drop_media_length + 1));
            selected_random_index === selected_drop_media_length ? random_items.push(selected_drop) : random_items.push(selected_drop.content[selected_random_index])
        }
        if (random_cards.length) {
            var random_card_index = Math.floor(Math.random() * (random_items.length - 1));
            random_items.splice(random_card_index, 0, random_cards.pop())
        }
        $scope.items = $scope.items.concat(random_items), $scope.ready = !0
    }, this.load_featured_drop()
}]), angular.module("dropApp").controller("AllCtrl", ["$scope", "$timeout", "Api", "ngProgress", function ($scope, $timeout, Api, ngProgress) {
    $scope.limit = 6, $scope.limit_per_page = 6, $scope.visible = !1, this.load_drops = function () {
        ngProgress.start(), Api.get_all_drops().then(function (response) {
            ngProgress.complete(), $timeout(function () {
                $scope.drops = response.reverse(), $scope.visible = !0
            }, 500)
        })
    }, $scope.background = "/images/auth/all.jpg", $scope.next_page = function () {
        $scope.limit = $scope.limit + $scope.limit_per_page
    }, this.load_drops()
}]), angular.module("dropApp").controller("CreateCtrl", ["$scope", "$q", "$timeout", "Api", "Tables", "ngProgress", function ($scope, $q, $timeout, Api, Tables, ngProgress) {
    $scope.card = {}, $scope.imageUrl = "spotify-thedrop.com", $scope.auth_blocked = false, $scope.auth_blocked || (ngProgress.start(), $scope.auth_blocked = !1, $q.all([Tables.get_user(), Api.get_all_drops()]).then(function (response) {
        ngProgress.complete(), $scope.card.drop = response[1][0], $scope.card.track = $scope.card.drop.song_list[0], $scope.$watch("card.drop", function () {
            $scope.card.track = $scope.card.drop.song_list[0], $scope.randomize()
        }, !0), $timeout(function () {
            $timeout(function () {
                $scope.drops = response[1], $scope.card.user = response[0]
            }, 500)
        }, 500)
    }, function (response) {
        ngProgress.complete(), $scope.auth_blocked = !0, console.log("error")
    })), $scope.themes = [{text: "#F59B23", foreground: "#AF2896", background: "#503750", value: 0}, {
        text: "#2D46B9",
        foreground: "#CDF564",
        background: "#FF4632",
        value: 1
    }, {text: "#F573A0", foreground: "#9BF0E1", background: "#503750", value: 2}, {
        text: "#000000",
        foreground: "#FAE62D",
        background: "#F037A5",
        value: 3
    }], $scope.background = "/images/auth/auth.png", $scope.bursts = ["/images/bursts/triangle.png", "/images/bursts/rectangle.png", "/images/bursts/circle.png"], $scope.select_type = function (type) {
        $scope.card.type = type
    }, $scope.select_burst = function (burst) {
        $scope.card.burst = burst
    }, $scope.select_image = function (image) {
        $scope.card.image = image
    }, $scope.select_theme = function (theme) {
        $scope.card.theme = theme
    }, $scope.scale_background = function () {
        $scope.card.scale = !$scope.card.scale
    }, $scope.flip_background = function () {
        $scope.card.flip = !$scope.card.flip
    }, $scope.rotate_background = function () {
        $scope.card.rotation = $scope.card.rotation < 3 ? ++$scope.card.rotation : 0
    }, $scope.drag_background = function () {
        $scope.drag = !$scope.drag
    }, $scope.randomize = function () {
        $scope.card.type = Math.random() >= .5 ? "burst" : "image", $scope.card.image = $scope.card.drop ? $scope.card.drop.card_images[Math.floor(Math.random() * $scope.card.drop.card_images.length)] : "", $scope.card.burst = $scope.bursts[Math.floor(Math.random() * $scope.bursts.length)], $scope.card.theme = $scope.themes[Math.floor(Math.random() * $scope.themes.length)], $scope.card.rotation = Math.floor(3 * Math.random()), $scope.card.flip = Math.random() >= .5, $scope.card.scale = Math.random() >= .5
    }
}]), angular.module("dropApp").controller("DropCtrl", ["$scope", "$routeParams", "$timeout", "$location", "Api", function ($scope, $routeParams, $timeout, $location, Api) {
    function load_drop() {
        isNaN($routeParams.id) ? Api.get_featured().then(render_drop) : Api.get_drop($routeParams.id).then(render_drop)
    }

    function render_drop(drop) {
        "i" === drop.dropped_at.charAt(0) || "i" === drop.dropped_at.charAt(5) ? drop.dropped = !1 : drop.dropped = !0, $scope.drop = drop, $scope.drop.album_uri = "spotify:album:" + $scope.drop.album_uri, $scope.drop.endpoint = "http://open.spotify.com/" + $scope.drop.album_uri.replace("spotify:", "").replace(/:/g, "/"), $scope.spotify = "/images/spotify.png";
        for (var i = $scope.drop.content.length; i--; i >= 0)$scope.drop.content[i].locked ? $scope.drop.content.splice($scope.drop.content.length, 0, $scope.drop.content.splice($scope.drop.content[i], 1)[0]) : $scope.items.unshift($scope.drop.content[i]);
        Api.get_ad().then(function (response) {
            $scope.items.push(response[0])
        }), $timeout(function () {
            $scope.$broadcast("timer-start")
        }, 400), $scope.$on("$routeUpdate", $scope.on_route_update), $scope.on_route_update()
    }

    $scope.items = [], $scope.clickWatch = 0, $scope.set_media = function (index) {
        $location.search("media", index), $scope.clickWatch++
    }, $scope.clear_media = function () {
        $(".youtube-player iframe").remove(), $location.url($location.path())
    }, $scope.on_route_update = function () {
        $scope.media_index = $location.search().media, $scope.media = $scope.drop.content[$scope.media_index]
    }, load_drop()
}]), angular.module("dropApp").directive("slider", function () {
    return {
        scope: {options: "=sliderOptions", switcher: "=clickWatch"},
        templateUrl: "/partials/slider.html",
        transclude: !0,
        controller: ["$scope", function ($scope) {
            this.initialize = function () {
                $scope.init()
            }
        }],
        link: function (scope, element) {
            var defaults = {
                grabCursor: !0,
                pagination: $(element).find(".swiper-pagination"),
                nextButton: $(element).find(".swiper-button-next"),
                prevButton: $(element).find(".swiper-button-prev"),
                paginationClickable: !0,
                preloadImages: !1,
                lazyLoading: !0
            };
            scope.$watch("switcher", function () {
                $(".swiper-slide .swiper-lazy").each(function (idx) {
                    self = $(this), $(this).attr("src") && (self.removeClass("swiper-lazy-loaded"), self.removeAttr("src"), setTimeout(function () {
                        s.lazy.load()
                    }))
                })
            });
            var s, options = angular.extend(defaults, scope.options);
            scope.init = function () {
                s && s.destroy(), setTimeout(function () {
                    s = new Swiper(element[0], options)
                }, 100)
            }
        }
    }
}), angular.module("dropApp").directive("sliderItem", function () {
    return {
        require: "^slider", link: function (scope, element, attrs, sliderCtrl) {
            scope.$last && sliderCtrl.initialize()
        }
    }
}), angular.module("dropApp").directive("mediaCircle", function () {
    return {
        templateUrl: "partials/media-circle.html", link: function (scope, element) {
            var r = 4 * Math.random() + 2.5, t = .25 * Math.PI, labelX = r + r * Math.cos(t), labelY = r + r * Math.sin(t);
            scope.circleStyle = {width: 2 * r + "em", height: 2 * r + "em"}, scope.labelStyle = {
                top: labelY + "em",
                left: labelX + "em"
            }, setTimeout(function () {
                element.addClass("revealed")
            }, 200 * scope.$index + 1500)
        }
    }
}), angular.module("dropApp").directive("imageLoader", function () {
    return {
        scope: {imageLoaderSrc: "=imageLoaderSrc"},
        template: '<div class="image-loader-element" ng-style="backgroundImage"></div><div class="image-loader-progress"></div>',
        link: function (scope, element, attrs) {
            function loadImage() {
                image = new Image, image.onload = onImageLoaded, image.src = scope.imageLoaderSrc, element.removeClass("image-loader-complete").addClass("image-loader-loading")
            }

            function onImageLoaded() {
                void 0 !== attrs.backgroundImage && scope.$emit("imageLoaded"), element.removeClass("image-loader-loading").addClass("image-loader-complete").children().first().css("background-image", "url(" + scope.imageLoaderSrc + ")")
            }

            function onImageError() {
                element.removeClass("image-loader-loading").removeClass("image-loader-complete")
            }

            var image;
            scope.$watch("imageLoaderSrc", function (val) {
                val ? setTimeout(function () {
                        loadImage()
                    }, 300) : onImageError()
            })
        }
    }
}), angular.module("dropApp").directive("gallery", function () {
    return {
        scope: {items: "=galleryItems"},
        templateUrl: "partials/gallery.html",
        link: function (scope, element, attrs) {
            scope.gallerySliderOptions = {
                slidesPerView: 1,
                pagination: ".swiper-pagination",
                nextButton: ".swiper-button-next",
                prevButton: ".swiper-button-prev",
                paginationClickable: !0,
                preloadImages: !1,
                lazyLoading: !0,
                loop: !0
            }
        }
    }
}), angular.module("dropApp").directive("youtubePlayer", ["$sce", "$timeout", function ($sce, $timeout) {
    return {
        scope: {data: "=youtubePlayer"},
        templateUrl: "partials/videoplayer.html",
        link: function (scope, element, attrs) {
            var id, base = "http://www.youtube.com/embed/{id}?autoplay=1&showinfo=0&rel=0&autohide=1&showinfo=0&enablejsapi=1&playerapiid=ytplaye";
            scope.visible = !1, scope.$watch("data", function () {
                if (scope.data && scope.data.content) {
                    if (!scope.data.content.url)return void scope.stop();
                    id = scope.data.content.url.split("watch?v=")[1], $timeout(scope.play, 500)
                } else scope.stop()
            }), scope.play = function () {
                scope.iframe_url = $sce.trustAsResourceUrl(base.replace("{id}", id)), scope.playing = !0
            }, scope.stop = function () {
                scope.iframe_url = $sce.trustAsResourceUrl("about:blank"), scope.playing = !1
            }, window.uploadDone = function () {
                scope.data && (scope.visible = !0, scope.$apply())
            }
        }
    }
}]), angular.module("dropApp").directive("packery", function () {
    return {
        scope: !0, controller: ["$scope", function ($scope) {
            this.initialize = function () {
                $scope.init()
            }
        }], link: function (scope, element, attrs) {
            var p, defaults = {
                itemSelector: ".block-item",
                percentPosition: !0,
                masonry: {columnWidth: ".block-item-small"}
            };
            scope.init = function () {
                setTimeout(function () {
                    p = new Isotope(element[0], defaults)
                }, 200), setTimeout(function () {
                    p.arrange()
                }, 300)
            }
        }
    }
}), angular.module("dropApp").directive("packeryItem", function () {
    return {
        require: "^packery", link: function (scope, element, attrs, packeryCtrl) {
            scope.$last && packeryCtrl.initialize()
        }
    }
}), angular.module("dropApp").directive("blockItem", function () {
    return {
        scope: {data: "=blockItemData"},
        template: '<div ng-include="get_template_url()"></div>',
        link: function (scope, element, attrs) {
            function generate_media_cta(type) {
                switch (type) {
                    case"video":
                        return "watch now";
                    case"playlist":
                        return "listen";
                    case"single":
                        return "listen";
                    case"gallery":
                        return "view now";
                    case"ad":
                        return "watch now";
                    default:
                        return "view " + type
                }
            }

            function get_image_url(id, files) {
                return files[id] ? files[id].url : null
            }

            function build_card() {
                switch (scope.data.type) {
                    case"card":
                        scope.template_url = "partials/block-item-card.html", scope.cta = "create your card", scope.image = get_image_url(scope.data.image_id, scope.data.files), element.addClass("block-item-small block-item-card");
                        break;
                    case"ad":
                        scope.template_url = "partials/block-item-ad.html", scope.cta = "", scope.ad_url = scope.data.ad_url, scope.tracking_pixel_url = scope.data.tracking_pixel_url, element.addClass("block-item-small block-item-ad");
                        break;
                    case"media":
                        scope.template_url = "partials/block-item-media.html", scope.cta = generate_media_cta(scope.data.media_type), element.addClass("video" === scope.data.media_type ? "block-item-large" : "block-item-small");
                        break;
                    case"drop":
                        scope.template_url = "partials/block-item-drop.html", scope.cta = "view drop", element.addClass("block-item-small")
                }
                if (!scope.data.image && !scope.data.featured_image) {
                    var random_drop_style = Math.floor(5 * Math.random());
                    element.addClass("drop-style-" + random_drop_style)
                }
            }

            scope.$watch("data", function (val) {
                val && build_card()
            }), scope.get_template_url = function () {
                return scope.template_url
            }, scope.show_ad_video = function (youtube_id) {
                if (youtube_id) {
                    var iframeHtml = '<iframe width="560" height="315" style="z-index: 2" src="http://www.youtube.com/embed/' + youtube_id + '?autoplay=1&showinfo=0" frameborder="0" allowfullscreen></iframe>';
                    $.fancybox(iframeHtml, {
                        title: "",
                        closeBtn: !1,
                        closeClick: !0,
                        width: 560,
                        height: 300,
                        autoSize: !1,
                        autoHeight: !1,
                        scrolling: !1,
                        helpers: {
                            overlay: {
                                closeClick: !0,
                                locked: !1,
                                css: {
                                    position: "fixed",
                                    width: "100%",
                                    height: "100%",
                                    top: 0,
                                    left: 0,
                                    background: "rgba(50,50,50, 0.7)",
                                    zIndex: 0
                                }
                            }
                        }
                    })
                }
            }, scope.is_playing = !1, scope.toggle_music = function () {
                scope.is_playing ? (scope.is_playing = !1, element.removeClass("block-item-playing"), scope.audio_uri = null) : (scope.is_playing = !0, element.addClass("block-item-playing"), scope.audio_uri = "spotify:track:1XALSFY5nrFQ9NaI2XNp9t")
            }
        }
    }
}), angular.module("dropApp").directive("dropCard", ["Image", "Api", function (Image, Api) {
    return {
        scope: !1, restrict: "A", link: function (scope, element, attrs) {
            function render_background() {
                var is_burst = "burst" === scope.card.type;
                background.onImageLoaded(function () {
                    var img_data = background.getOriginalImageData();
                    img_data = Image.duotone(img_data, scope.card.theme.background, scope.card.theme.foreground), img_data = scope.card.flip ? Image.flipImage(img_data) : img_data, background.setImageData(img_data), background.setRotation(is_burst ? 90 * scope.card.rotation : 0), background.update(!is_burst && scope.card.scale ? {
                            w: 900,
                            h: 900,
                            x: -300,
                            y: -300
                        } : {w: 450, h: 450, x: -75, y: -75})
                }), card.background.update({fill: scope.card.theme.background}), background.changeURL(is_burst ? scope.card.burst : scope.card.image)
            }

            function render_text() {
                return scope.card.track ? (text.song.changeText(scope.card.track), text.songxs.changeText(""), text.artist.changeText(scope.card.drop.album), text.song.w > 270 ? (text.song.changeText(""), text.songxs.changeText(scope.card.track), format_text("small"), text.songxs.w > 270 && text.songxs.truncate(270)) : format_text("normal"), void(text.artist.w > 200 && text.artist.truncate(200))) : !1
            }

            function format_text(type) {
                switch (type) {
                    case"small":
                        text.headline.update({y: 100}), text.songxs.update({y: 138}), text.from.update({y: 170}), text.artist.update({y: 170});
                        break;
                    default:
                        text.headline.update({y: 100}), text.song.update({y: 145}), text.from.update({y: 178}), text.artist.update({y: 178})
                }
                for (var t in text)text[t].update({fill: scope.card.theme.text})
            }

            function render_user() {
                if (scope.card.user) {
                    var img = new window.Image;
                    img.crossOrigin = "Anonymous", img.onload = function () {
                        var c = document.createElement("canvas"), ctx = c.getContext("2d");
                        ctx.beginPath(), ctx.arc(25, 25, 25, 0, 2 * Math.PI, !0), ctx.closePath(), ctx.fill(), ctx.clip(), ctx.drawImage(this, 0, 0, 50, 50);
                        var d = ctx.getImageData(0, 0, 50, 50);
                        avatar.setImageData(d), avatar.update({y: 15})
                    }, img.src = scope.card.user.images[0].url, username.changeText(scope.card.user.display_name), username.update({y: 35})
                }
            }

            function onMouseLeave() {
                is_dragging = !1
            }

            function onMouseDown(x, y) {
                drag_position.x = x - background.x, drag_position.y = y - background.y, is_dragging = !0
            }

            function onMouseUp() {
                is_dragging = !1
            }

            function onMouseMove(x, y) {
                if (is_dragging) {
                    var _x = x - drag_position.x, _y = y - drag_position.y;
                    _x >= -1 ? _x = -1 : _x + background.w <= card.width - 1 && (_x = card.width - background.w + 1), _y >= -1 ? _y = -1 : _y + background.h <= card.height - 1 && (_y = card.height - background.h + 1), background.update({
                        x: _x,
                        y: _y
                    })
                }
            }

            var card = new Postcard(element[0], {filename: "dropcard.png"}), background = card.addImage("background", ""), avatar = (card.addImage("bottomLogos", "/images/bottomlogos.png", {
                x: 0,
                y: 260,
                w: 300,
                h: 40
            }), card.addImage("avatar", "/images/sample-profile.png", {
                x: 15,
                y: -45,
                w: 35,
                h: 35
            })), username = card.addText("username", "", 10, {
                x: 58,
                y: -35,
                family: "light",
                size: "14px"
            }), text = {
                headline: card.addText("intro", "I'm feeling", 10, {x: 15, y: -15, family: "light", size: "20px"}),
                song: card.addText("song", "", 10, {x: 15, y: 128, family: "bold", size: "40px"}),
                songxs: card.addText("song.small", "", 10, {x: 15, y: 121, family: "bold", size: "30px"}),
                from: card.addText("from", "from", 10, {x: 15, y: -15, family: "light", size: "20px"}),
                artist: card.addText("artist", "", 10, {x: 63, y: -15, family: "bold", size: "20px"}),
                cta: card.addText("cta", "Album out now", 10, {x: 15, y: -15, family: "light", size: "15px"})
            };
            scope.refresh = function () {
                scope.saved = !1, render_background(), render_text(), render_user()
            }, scope.$watch("card", scope.refresh, !0), scope.randomize();
            var is_dragging = !1, drag_position = {};
            scope.$watch("drag", function () {
                scope.drag ? (element.css("cursor", "move").bind("mouseleave", onMouseLeave), card.onMouseDown(onMouseDown), card.onMouseUp(onMouseUp), card.onMouseMove(onMouseMove)) : (element.css("cursor", "auto"), card.clearMouseEvents())
            }), scope.save = function () {
                Api.save_card(scope.card, card["export"]()).then(function (response) {
                    scope.saved = !0, Api.get_single_card(response).then(function (data) {
                        scope.imageUrl = encodeURIComponent(data.files[data.image_id].url)
                    })
                })
            }, scope.download = function (event) {
                card.save(event)
            }, PostcardTextObject.prototype.truncate = function (width) {
                for (var t = this.text, i = 0; this.w > width;)this.changeText(t.substr(0, t.length - ++i) + "...")
            }
        }
    }
}]), angular.module("dropApp").directive("mediaIcon", function () {
    return {
        scope: {media: "=mediaIcon"}, link: function (scope, element, attrs) {
            scope.$watch("media", function () {
                if (element.removeClass(), scope.media.locked) element.replaceWith('<div class="exlusive-video"><img src="/images/icons/Locked-white.svg"><span>Locked Content</span></div>'); else switch (scope.media.media_type) {
                    case"video":
                        element.replaceWith('<div class="exlusive-video"><img src="/images/icons/Video-white.svg"><span>Exclusive Video</span></div>');
                        break;
                    case"playlist":
                        element.replaceWith('<div class="exlusive-video"><img src="/images/icons/Playlist-white.svg"><span>Exclusive Playlist</span></div>');
                        break;
                    case"single":
                        element.addClass("fa fa-play");
                        break;
                    case"gallery":
                        element.replaceWith('<div class="exlusive-video"><img src="/images/icons/Gallery-white.svg"><span>Exclusive Gallery</span></div>')
                }
            })
        }
    }
});







var defaultDrop = [
    {
        "id": 2,
        "name": "Welcome to Taboola Tables!",
        "description": "Eat with other people and get rated!",
        "album": "It's Fun AND delicious!",
        "header_image_id": 2,
        "featured_image_id": 2,
        "featured": 2,
        "files": {
            "2": {
                "id": 2,
                "url": "http:\/\/www.yolks.ca\/wp-content\/uploads\/2015\/03\/slider2.jpg"
            }
        },
        "content": [{
            "id": 300,
            "drop_id": 300,
            "title": "First Person",
            "description": "Likes to talk about AAA",
            "content_type": "playlist",
            "featured_image_id": 3,
            "files": {
                "3": {
                    "id": 3,
                    "url": "https:\/\/spotify-thedrop.s3.amazonaws.com\/drops\/2016-07\/af-d159a18af1d395c94f24e1130520c450.jpg"
                }
            }
        },
            {
                "id": 400,
                "drop_id": 400,
                "title": "Second Person",
                "description": "Likes to talk about BBB",
                "content_type": "playlist",
                "featured_image_id": 4,
                "files": {
                    "4": {
                        "id": 4,
                        "url": "https:\/\/spotify-thedrop.s3.amazonaws.com\/drops\/2016-07\/af-0fa34211b34b5e11660162d9c97aba6e.jpg"
                    }
                }
            },
            {
                "id": 500,
                "drop_id": 500,
                "title": "Third Person",
                "description": "Likes to talk about CCC",
                "content_type": "single",
                "featured_image_id": 5,
                "files": {
                    "5": {
                        "id": 5,
                        "url": "https:\/\/spotify-thedrop.s3.amazonaws.com\/drops\/2016-07\/af-be77563d5ec9ddd021de0fcc6e59d061.jpg"
                    }
                }
            },
            {
                "id": 600,
                "drop_id": 600,
                "title": "Fourth Person",
                "description": "Likes to talk about DDD",
                "content_type": "playlist",
                "featured_image_id": 6,
                "files": {
                    "6": {
                        "id": 6,
                        "url": "https:\/\/spotify-thedrop.s3.amazonaws.com\/drops\/2016-07\/af-17b58aac96bc6cde56fd151341b7b3c0.jpg"
                    }
                }
            }]
    }
];