<template>
	<div class="xn-pb10">
		<a-row :gutter="10">
			<a-col :xs="12" :sm="12" :md="6" :lg="6" :xl="6">
				<a-card class="wqs-monitor-card" :bordered="false">
					<template #cover>
						<team-outlined style="color: #69c0ff" class="wqs-monitor-card-icon" />
						<div class="wqs-monitor-card-div">
							<span class="wqs-monitor-card-span">当前会话数：</span
							><span class="wqs-monitor-card-span">{{ analysisObj.currentSessionTotalCount }}</span>
						</div>
					</template>
				</a-card>
			</a-col>
			<a-col :xs="12" :sm="12" :md="6" :lg="6" :xl="6">
				<a-card class="wqs-monitor-card" :bordered="false">
					<template #cover>
						<verified-outlined class="wqs-monitor-card-icon xn-color-ff9c6e" />
						<div class="wqs-monitor-card-div">
							<span class="wqs-monitor-card-span">最大签发令牌：</span
							><span class="wqs-monitor-card-span">{{ analysisObj.maxTokenCount }}</span>
						</div>
					</template>
				</a-card>
			</a-col>
			<a-col :xs="12" :sm="12" :md="6" :lg="6" :xl="6">
				<a-card class="wqs-monitor-card" :bordered="false">
					<template #cover>
						<rise-outlined class="wqs-monitor-card-icon xn-color-ff85c0" />
						<div class="wqs-monitor-card-div">
							<span class="wqs-monitor-card-span">1小时内新增：</span
							><span class="wqs-monitor-card-span">{{ analysisObj.oneHourNewlyAdded }}</span>
						</div>
					</template>
				</a-card>
			</a-col>
			<a-col :xs="12" :sm="12" :md="6" :lg="6" :xl="6">
				<a-card class="wqs-monitor-card" :bordered="false">
					<template #cover>
						<pie-chart-outlined class="wqs-monitor-card-icon xn-color-5cdbd3" />
						<div class="wqs-monitor-card-div">
							<span class="wqs-monitor-card-span">B/C端占比：</span
							><span class="wqs-monitor-card-span">{{ analysisObj.proportionOfBAndC }}</span>
						</div>
					</template>
				</a-card>
			</a-col>
		</a-row>
	</div>
</template>

<script setup name="monitorAnalyse">
	import monitorApi from '@/api/auth/monitorApi'

	// 预置空数据
	const analysisObj = ref({
		currentSessionTotalCount: '0',
		maxTokenCount: '0',
		oneHourNewlyAdded: '0',
		proportionOfBAndC: '0/0'
	})
	monitorApi.monitorAnalysis().then((res) => {
		analysisObj.value = res
	})
</script>

<style scoped>
	.wqs-monitor-card {
		height: 100px;
		margin-bottom: 4px;
	}
	.wqs-monitor-card-icon {
		font-size: 30px;
		padding-top: 18px;
		padding-bottom: 5px;
	}
	.wqs-monitor-card-div {
		display: flex;
		justify-content: center;
	}
	.wqs-monitor-card-span {
		font-size: 16px;
	}
	.xn-color-ff9c6e {
		color: #ff9c6e;
	}
	.xn-color-ff85c0 {
		color: #ff85c0;
	}
	.xn-color-5cdbd3 {
		color: #5cdbd3;
	}
</style>
