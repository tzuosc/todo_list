interface ColumnProps {
    tasks: string[];
    loading?: boolean;
}

export function Column({ tasks, loading }: ColumnProps) {
    if (loading) return <div>加载中...</div>;

    return (
        <div className="border rounded shadow bg-white">
            <h2 className="text-xl font-semibold mb-2">任务列表</h2>
            <ul className="space-y-2">
                {tasks.map((taskName, idx) => (
                    <li key={idx} className="border rounded bg-gray-100">
                        {taskName}
                    </li>
                ))}
            </ul>
        </div>
    );
}
