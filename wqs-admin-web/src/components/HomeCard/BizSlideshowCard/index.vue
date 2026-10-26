<template>
	<a-card :bordered="false" :loading="apiLoading" class="slideshow-card" :body-style="{ padding: '0px' }">
		<a-carousel v-if="slideshowList.length > 0" autoplay class="slideshow-carousel">
			<div v-for="item in slideshowList" :key="item.id" class="slideshow-item">
				<img
					v-if="!item.imgError"
					:src="item.image"
					:alt="item.title"
					:class="{ clickable: isClickable(item) }"
					@error="onImgError(item)"
					@click="handleItemClick(item)"
				/>
				<div v-else class="img-error"><a-empty :image="SimpleImage" description="图片加载失败" /></div>
				<div v-if="item.title" class="slideshow-title">{{ item.title }}</div>
			</div>
		</a-carousel>
		<a-empty v-else-if="!apiLoading" description="暂无轮播图" />
	</a-card>
</template>

<script setup name="slideshowCard">
	import slideshowApi from '@/api/dev/slideshowApi'
	import { Empty } from 'ant-design-vue'
	import { useRouter } from 'vue-router'

	const SimpleImage = Empty.PRESENTED_IMAGE_SIMPLE

	const router = useRouter()

	const props = defineProps({
		config: {
			type: Object,
			default: () => ({})
		}
	})

	const apiLoading = ref(false)
	const slideshowList = ref([])

	// 获取指定位置的启用轮播图
	const loadData = () => {
		const place = props.config?.options?.place
		if (!place) {
			slideshowList.value = []
			return
		}
		apiLoading.value = true
		slideshowApi
			.devSlideshowGetListByPlace({ place })
			.then((data) => {
				slideshowList.value = data || []
			})
			.catch(() => {})
			.finally(() => {
				apiLoading.value = false
			})
	}

	// 配置（展示位置）变化时重新加载
	watch(
		() => props.config,
		() => {
			loadData()
		},
		{ immediate: true, deep: true }
	)

	// 图片加载失败（如文件已被删除）时占位展示，避免裂图
	const onImgError = (item) => {
		item.imgError = true
	}

	// 该轮播是否配置了点击跳转
	const isClickable = (item) => item.pathDetails?.whetherToClick === 'ENABLE' && !!item.pathDetails?.url

	// 点击跳转：URL方式（http链接）新窗口打开，其余按内部路由地址跳转
	const handleItemClick = (item) => {
		if (!isClickable(item)) {
			return
		}
		const { url, skipMode } = item.pathDetails
		if (skipMode === 'URL' || /^https?:\/\//i.test(url)) {
			window.open(url, '_blank')
		} else {
			router.push(url)
		}
	}
</script>

<style scoped lang="less">
	.slideshow-card {
		height: 100%;
		:deep(.ant-card-body) {
			height: 100%;
		}
	}
	.slideshow-carousel {
		height: 220px;
		:deep(.slick-slider) {
			height: 100%;
		}
		:deep(.slick-list) {
			height: 100% !important;
		}
		:deep(.slick-track) {
			height: 100%;
		}
	}
	.slideshow-item {
		position: relative;
		height: 220px;
		overflow: hidden;
		img {
			width: 100%;
			height: 100%;
			object-fit: cover;
			display: block;
			cursor: default;
			&.clickable {
				cursor: pointer;
			}
		}
	}
	.slideshow-title {
		position: absolute;
		left: 0;
		right: 0;
		bottom: 0;
		padding: 24px 12px 10px;
		background: linear-gradient(180deg, transparent, rgba(0, 0, 0, 0.55));
		color: #fff;
		font-size: 14px;
		text-align: left;
		pointer-events: none;
	}
	.img-error {
		height: 100%;
		display: flex;
		align-items: center;
		justify-content: center;
	}
</style>
